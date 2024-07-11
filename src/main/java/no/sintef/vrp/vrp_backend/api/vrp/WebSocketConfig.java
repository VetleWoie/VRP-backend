package no.sintef.vrp.vrp_backend.api.vrp;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

import static org.drools.core.management.DroolsManagementAgent.logger;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(), "/ws").setAllowedOrigins("*").addInterceptors(getInter());
    }

    private HandshakeInterceptor getInter() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, org.springframework.web.socket.WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                logger.info("Websocket Request ------- {}", request.getHeaders());
                String query = request.getURI().getQuery();
                if (query != null && query.contains("problem_id=")) {
                    String[] params = query.split("&");
                    for (String param : params) {
                        if (param.startsWith("problem_id=")) {
                            String problemId = param.substring("problem_id=".length());
                            attributes.put("problem_id", Long.parseLong(problemId));
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, org.springframework.web.socket.WebSocketHandler wsHandler, Exception exception) {

            }
        };
    }
}
