package no.sintef.vrp.vrp_backend.api.vrp;

import jakarta.annotation.Resource;
import no.sintef.vrp.vrp_backend.vrp.domain.VehicleRoutingSolution;
import no.sintef.vrp.vrp_backend.vrp.persistance.VehicleRoutingSolutionRepository;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.solver.SolutionManager;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/solver")
public class SolverController {
    private static final long PROBLEM_ID = 0L;

    private final AtomicReference<Throwable> solverError = new AtomicReference<>();

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
                solverManager.getSolverStatus(PROBLEM_ID));
    }

    @PostMapping("/solve")
    public void solve() {
        Optional<VehicleRoutingSolution> maybeSolution = repository.solution();
        maybeSolution.ifPresent(
                vehicleRoutingSolution -> solverManager.solveAndListen(PROBLEM_ID, id -> vehicleRoutingSolution,
                        repository::update, (problemId, throwable) -> solverError.set(throwable)));
    }

    @GetMapping("/status")
    /**
     * Only one status per now. This needs to be fixed: TODO
     */
    public SolverStatus status() {
        Optional.ofNullable(solverError.getAndSet(null)).ifPresent(throwable -> {
            throw new RuntimeException("Solver failed", throwable);
        });

        Optional<VehicleRoutingSolution> s1 = repository.solution();

        VehicleRoutingSolution s = s1.orElse(VehicleRoutingSolution.empty());
        return statusFromSolution(s);
    }

    @PostMapping("/stopSolving")
    public void stopSolving() {
        solverManager.terminateEarly(PROBLEM_ID);
    }
}
