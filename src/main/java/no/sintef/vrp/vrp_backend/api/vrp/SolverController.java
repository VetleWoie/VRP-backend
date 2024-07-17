package no.sintef.vrp.vrp_backend.api.vrp;

import no.sintef.vrp.vrp_backend.vrp.domain.*;
import no.sintef.vrp.vrp_backend.vrp.persistance.RoutingPlanSolutionRepository;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.solver.SolutionManager;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static org.drools.core.management.DroolsManagementAgent.logger;

@RestController
@RequestMapping("/api/solver")
public class SolverController {
    private static long PROBLEM_ID = 0L;

    private final AtomicReference<Throwable> solverError = new AtomicReference<>();

    @Autowired
    private WebSocketHandler webSocketHandler;

    private final RoutingPlanSolutionRepository repository;
    @Autowired
    private final SolverManager<RoutingPlan, Long> solverManager;
    @Autowired
    private final SolutionManager<RoutingPlan, HardSoftLongScore> solutionManager;

    public SolverController(RoutingPlanSolutionRepository repository,
                            SolverManager<RoutingPlan, Long> solverManager,
                            SolutionManager<RoutingPlan, HardSoftLongScore> solutionManager) {
        this.repository = repository;
        this.solverManager = solverManager;
        this.solutionManager = solutionManager;
    }

    private SolverStatus statusFromSolution(RoutingPlan solution) {
        System.out.println(solutionManager.explain(solution).getSummary());
        System.out.println(solverManager.getSolverStatus(solution.getId()));
        return new SolverStatus(solution,
                solutionManager.explain(solution).getSummary(),
                solverManager.getSolverStatus(solution.getId()));
    }

    @PostMapping(
            value="/problem",
            consumes="application/json"
    )
    public Long createProblem(
            @RequestBody schema.NewProblem newProblem
    ){
        Location[] locations = IntStream
                .range(0, newProblem.locations.size())
                .mapToObj((idx) -> {
                    schema.LocationInput loc = newProblem.locations.get(idx);
                    return new Location(
                            idx,
                            loc.getName(),
                            loc.getDistances().toArray(new Integer[0])
                    );
                }).toArray(Location[]::new);

        List<Vehicle> vehicles = newProblem.vehicleInputList.stream().map(
                (vehicle) -> new Vehicle(
                        vehicle.getIdx(),
                        locations[vehicle.getIdx()],
                        vehicle.getCapacity()
                )
        ).toList();
        List<DropOffPoint> dropOffs = newProblem.dropOffInputList.stream().map(
                (dropOff) -> new DropOffPoint(
                        dropOff.getIdx(),
                        locations[dropOff.getIdx()],
                        dropOff.getAmountNeeded()
                )
        ).toList();

        List<PickupPoint> pickUps = newProblem.pickUpInputList.stream().map(
                (pickUp) -> new PickupPoint(
                        pickUp.getIdx(),
                        locations[pickUp.getIdx()],
                        pickUp.getAmountAvailable()
                )
        ).toList();

        //TODO: Need to figure out amount of tasks needed.
        List<Task> tasks = new ArrayList<>();
        //Currently assuming all vehicles have the same capacity
        int vehicleCap = newProblem.vehicleInputList.get(0).capacity;
        long id = 0L;
        //This might be a bit naive, but currently it will be stay like this
        for (DropOffPoint dropOffPoint : dropOffs) {
            int remaingCapacity = dropOffPoint.getAmountNeeded();
            while (remaingCapacity > 0) {
                tasks.add(new Task(
                        id++,
                        dropOffPoint.getLocation(),
                        Math.min(remaingCapacity, vehicleCap),
                        false
                ));
                remaingCapacity -= vehicleCap;
            }
        }

        for (PickupPoint pickUpPoint : pickUps) {
            int remaingCapacity = pickUpPoint.getAmountAvailable();
            while (remaingCapacity > 0) {
                tasks.add(new Task(
                        id++,
                        pickUpPoint.getLocation(),
                        Math.min(remaingCapacity, vehicleCap),
                        true
                ));
                remaingCapacity -= vehicleCap;
            }
        }

        repository.update(
                new RoutingPlan(
                        PROBLEM_ID,
                        vehicles,
                        tasks,
                        pickUps,
                        dropOffs
                ),
                PROBLEM_ID
        );
        return PROBLEM_ID++;
    }

    @PostMapping(value="/solve")
    public void solve(@RequestParam long problem_id) {
        RoutingPlanSolverEventListener solverEventListener = new RoutingPlanSolverEventListener(problem_id, webSocketHandler);
        Optional<RoutingPlan> maybeSolution = repository.solution(problem_id);
        RoutingPlan routingPlan = maybeSolution.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solution not found"));
        logger.info("Starting new solver");
        solverManager.solveAndListen(
                routingPlan.getId(),
                id -> routingPlan,
                bestSolution -> {
                    repository.update(bestSolution, bestSolution.getId());
                    solverEventListener.bestSolutionChanged(bestSolution);
                },
                (problemId, throwable) -> solverError.set(throwable));
    }

    @GetMapping("/status")
    public SolverStatus status(@RequestParam long problemId) {
        Optional.ofNullable(solverError.getAndSet(null)).ifPresent(throwable -> {
            System.out.println("Error:");
            System.out.println(throwable);
            throw new RuntimeException("Solver failed", throwable);
        });
        Optional<RoutingPlan> maybeSolution = repository.solution(problemId);
        RoutingPlan solution = maybeSolution.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solution not found"));
        return statusFromSolution(solution);
    }

    @PostMapping("/stopSolving")
    public void stopSolving(@RequestParam long problemId) {
        solverManager.terminateEarly(problemId);
    }
}
