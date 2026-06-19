package at.simcc.simcc_backend.api.ws;

import at.simcc.simcc_backend.api.sse.InfectedSSEComponent;
import at.simcc.simcc_backend.repo.InfectedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import java.util.Map;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/14/26
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final InfectedRepository infectedRepository;
    private final WebsocketHandler websocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(websocketHandler, "/ws/infected")
                .addInterceptors(new AuthInterceptor(infectedRepository))
                .setAllowedOrigins("*");
    }
}