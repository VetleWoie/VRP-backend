package no.sintef.vrp.vrp_backend.vrp.domain;

import no.sintef.vrp.vrp_backend.api.vrp.WebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.drools.core.management.DroolsManagementAgent.logger;

public class RoutingPlanSolverEventListener {

    private final WebSocketHandler webSocketHandler;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Long problemId;

    public RoutingPlanSolverEventListener(Long problemId, WebSocketHandler webSocketHandler) {
        this.problemId = problemId;
        this.webSocketHandler = webSocketHandler;
    }

    public void bestSolutionChanged(RoutingPlan newBestSolution) {
        try {
            String message = String.format(
                    "{feasable: %B, score: {hard: %d, soft: %d}}",
                    newBestSolution.getScore().isFeasible(),
                    newBestSolution.getScore().hardScore(),
                    newBestSolution.getScore().softScore()
            );
            this.webSocketHandler.sendMessage(problemId, message);
            logger.info("SolverEvent ----- Found new best solution: Feasable: {} Score: {}" , newBestSolution.getScore().isFeasible(), newBestSolution.getScore());
        } catch (Exception e) {
            logger.info("SolverEvent ----- Could not send new best solution due to error: {}", e.getMessage());
        }
    }
}
