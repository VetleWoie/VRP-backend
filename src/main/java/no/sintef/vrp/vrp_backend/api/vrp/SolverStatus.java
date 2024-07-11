package no.sintef.vrp.vrp_backend.api.vrp;

import no.sintef.vrp.vrp_backend.vrp.domain.VehicleRoutingSolution;

public class SolverStatus {

    public final VehicleRoutingSolution solution;
    public final String scoreExplanation;
    public final boolean isSolving;

    SolverStatus(VehicleRoutingSolution solution, String scoreExplanation, org.optaplanner.core.api.solver.SolverStatus solverStatus) {
        this.solution = solution;
        this.scoreExplanation = scoreExplanation;
        this.isSolving = solverStatus != org.optaplanner.core.api.solver.SolverStatus.NOT_SOLVING;
    }
}

