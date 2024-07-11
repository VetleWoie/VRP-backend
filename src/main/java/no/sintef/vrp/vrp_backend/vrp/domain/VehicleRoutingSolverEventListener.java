package no.sintef.vrp.vrp_backend.vrp.domain;

import no.sintef.vrp.vrp_backend.api.vrp.WebSocketHandler;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.drools.core.management.DroolsManagementAgent.logger;

public class VehicleRoutingSolverEventListener {

    private WebSocketHandler webSocketHandler;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Long problemId;

    public VehicleRoutingSolverEventListener(Long problemId, WebSocketHandler webSocketHandler) {
        this.problemId = problemId;
        this.webSocketHandler = webSocketHandler;
    }

    public void bestSolutionChanged(VehicleRoutingSolution newBestSolution) {
        try {
            String message = objectMapper.writeValueAsString(newBestSolution);
            newBestSolution.
            webSocketHandler.sendMessage(problemId, message);
            System.out.println("New best solution found for problem " + problemId + " and pushed to WebSocket with score: " + newBestSolution.getScore());
        } catch (Exception e) {
            logger.info("SolverEvent ----- Could not send new best solution due to error: {}", e.getMessage());
        }
    }
}
