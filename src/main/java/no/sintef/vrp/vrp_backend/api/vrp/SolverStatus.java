package no.sintef.vrp.vrp_backend.api.vrp;

import no.sintef.vrp.vrp_backend.vrp.domain.RoutingPlan;

public class SolverStatus {

    public final RoutingPlan solution;
    public final String scoreExplanation;
    public final boolean isSolving;

    SolverStatus(RoutingPlan solution, String scoreExplanation, org.optaplanner.core.api.solver.SolverStatus solverStatus) {
        System.out.println("Status: " + solverStatus);
        this.solution = solution;
        this.scoreExplanation = scoreExplanation;
        this.isSolving = solverStatus != org.optaplanner.core.api.solver.SolverStatus.NOT_SOLVING;
    }
}

