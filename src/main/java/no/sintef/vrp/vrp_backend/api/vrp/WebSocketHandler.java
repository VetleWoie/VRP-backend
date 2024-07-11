package no.sintef.vrp.vrp_backend.api.vrp;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

import static org.drools.core.management.DroolsManagementAgent.logger;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private static Map<Long, Set<WebSocketSession>> subscriptions = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long problem_id = (Long) session.getAttributes().get("problem_id");
        logger.info("WebSocket Connection ------- Established on problem ID: {}", problem_id);
        subscribe(session, problem_id);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //no-op
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long problem_id = (Long) session.getAttributes().get("problem_id");
        logger.info("WebSocket Connection ------- Closed on problem ID: {}", problem_id);
        unsubscribe(session, problem_id);
    }

    private void subscribe(WebSocketSession session, Long id) {
        subscriptions.computeIfAbsent(id, k -> {
            logger.info("Websocket subscribtions ------- New subscription for problem ID: {}", id);
            return new HashSet<>();
        }).add(session);
    }

    private void unsubscribe(WebSocketSession session, Long id) {
        Set<WebSocketSession> sessions = subscriptions.get(id);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                logger.info("Websocket subscribtions ------- Empty removing set");
                subscriptions.remove(id);
            }
        }
    }

    private void unsubscribeFromAll(WebSocketSession session) {
        for (Set<WebSocketSession> sessions : subscriptions.values()) {
            sessions.remove(session);
        }
    }

    public void sendMessage(Long id, String message) throws IOException {
        Set<WebSocketSession> sessions = subscriptions.get(id);
        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            }
        }
    }
}
