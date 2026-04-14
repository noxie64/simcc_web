package at.simcc.simcc_backend.api.ws;

import at.simcc.simcc_backend.repo.InfectedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

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

    @Value("${simcc.domain}")
    private String domain;

    @Override
    public void registerWebSocketHandlers(
            WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebsocketHandler(infectedRepository), "/infected/ws")
                .setAllowedOrigins(domain);
    }
}