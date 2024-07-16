package no.sintef.vrp.vrp_backend.api.vrp;

import no.sintef.vrp.vrp_backend.vrp.domain.Location;
import no.sintef.vrp.vrp_backend.vrp.domain.RoutingPlan;
import no.sintef.vrp.vrp_backend.vrp.domain.RoutingPlanSolverEventListener;
import no.sintef.vrp.vrp_backend.vrp.persistance.RoutingPlanSolutionRepository;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.solver.SolutionManager;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
        return new SolverStatus(solution,
                solutionManager.explain(solution).getSummary(),
                solverManager.getSolverStatus(solution.getId()));
    }

    @PostMapping(
            value="/solution",
            consumes="application/json"
    )
    public Long createSolution(
            @RequestBody schema.NewProblem newProblem
    ){
        System.out.println(newProblem);
        //Todo create problem on demand
        return 0L;
    }

    @PostMapping(value="/solve")
    public long solve(@RequestParam long problem_id) {
        RoutingPlanSolverEventListener solverEventListener = new RoutingPlanSolverEventListener(problem_id, webSocketHandler);
        Optional<RoutingPlan> maybeSolution = repository.solution(problem_id);
        System.out.println(maybeSolution.isPresent());
        maybeSolution.ifPresent(
                routingPlan -> {
                    //TODO: Remove this print, it was used only for testing purposes.
                    System.out.println("Calculating Solution...");
                    solverManager.solveAndListen(
                            routingPlan.getId(),
                            id -> routingPlan,
                            bestSolution -> {
                                repository.update(bestSolution, bestSolution.getId());
                                solverEventListener.bestSolutionChanged(bestSolution);
                            },
                            (problemId, throwable) -> solverError.set(throwable));
                }
        );
        return PROBLEM_ID;
    }

    @GetMapping("/status")
    public SolverStatus status(@RequestParam long problemId) {
        Optional.ofNullable(solverError.getAndSet(null)).ifPresent(throwable -> {
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
