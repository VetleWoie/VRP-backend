package no.sintef.vrp.vrp_backend.api.vrp;

import no.sintef.vrp.vrp_backend.vrp.domain.Location;
import no.sintef.vrp.vrp_backend.vrp.domain.MockDataBuilder;
import no.sintef.vrp.vrp_backend.vrp.domain.VehicleRoutingSolution;
import no.sintef.vrp.vrp_backend.vrp.domain.VehicleRoutingSolverEventListener;
import no.sintef.vrp.vrp_backend.vrp.persistance.VehicleRoutingSolutionRepository;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.solver.SolutionManager;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/solver")
public class SolverController {
    private static long PROBLEM_ID = 0L;

    private final AtomicReference<Throwable> solverError = new AtomicReference<>();

    @Autowired
    private WebSocketHandler webSocketHandler;

    private final VehicleRoutingSolutionRepository repository;
    @Autowired
    private final SolverManager<VehicleRoutingSolution, Long> solverManager;
    @Autowired
    private final SolutionManager<VehicleRoutingSolution, HardSoftLongScore> solutionManager;

    public SolverController(VehicleRoutingSolutionRepository repository,
                          SolverManager<VehicleRoutingSolution, Long> solverManager,
                          SolutionManager<VehicleRoutingSolution, HardSoftLongScore> solutionManager) {
        this.repository = repository;
        this.solverManager = solverManager;
        this.solutionManager = solutionManager;
    }

    private SolverStatus statusFromSolution(VehicleRoutingSolution solution) {
        return new SolverStatus(solution,
                solutionManager.explain(solution).getSummary(),
                solverManager.getSolverStatus(solution.getName()));
    }

    @PostMapping("/solution")
    public Long createSolution(
                               @RequestParam int customerCount,
                               @RequestParam int depotCount,
                               @RequestParam int vehicleCount,
                               @RequestParam int vehicleCap,
                               @RequestParam int maxDemand,
                               @RequestParam int minDemand){
        System.out.println("Create solution");
        var testbuilder = MockDataBuilder.builder();
        testbuilder.setCustomerCount(customerCount)
                .setDepotCount(depotCount)
                .setVehicleCount(vehicleCount)
                .setVehicleCapacity(vehicleCap)
                .setMaxDemand(maxDemand)
                .setMinDemand(minDemand)
                .setSouthWestCorner(new Location(0L,
                        69.640282, 18.87))
                .setNorthEastCorner(new Location(0L, 69.66, 18.98));
        var solution = testbuilder.build();
        solution.setName(PROBLEM_ID++);
        System.out.println(solution.getName());
        repository.update(solution, solution.getName());
        return solution.getName();
    }

    @PostMapping("/solve")
    public long solve(@RequestParam long problem_id) {
        VehicleRoutingSolverEventListener solverEventListener = new VehicleRoutingSolverEventListener(problem_id, webSocketHandler);
        Optional<VehicleRoutingSolution> maybeSolution = repository.solution(problem_id);
        System.out.println(maybeSolution.isPresent());
        maybeSolution.ifPresent(
                vehicleRoutingSolution -> {
                    //TODO: Remove this print, it was used only for testing purposes.
                    System.out.println("Calculating Solution...");
                    solverManager.solveAndListen(
                            vehicleRoutingSolution.getName(),
                            id -> vehicleRoutingSolution,
                            bestSolution -> {
                                repository.update(bestSolution, bestSolution.getName());
                                solverEventListener.bestSolutionChanged(bestSolution);
                            },
                            (problemId, throwable) -> solverError.set(throwable));
                }
        );
        return PROBLEM_ID;
    }

    @GetMapping("/status")
    /**
     * Only one status per now. This needs to be fixed: TODO
     */
    public SolverStatus status(@RequestParam long problemId) {
        Optional.ofNullable(solverError.getAndSet(null)).ifPresent(throwable -> {
            throw new RuntimeException("Solver failed", throwable);
        });
        Optional<VehicleRoutingSolution> maybeSolution = repository.solution(problemId);
        VehicleRoutingSolution s = maybeSolution.orElse(VehicleRoutingSolution.empty());
        return statusFromSolution(s);
    }

    @PostMapping("/stopSolving")
    public void stopSolving(@RequestParam long problemId) {
        solverManager.terminateEarly(problemId);
    }
}
