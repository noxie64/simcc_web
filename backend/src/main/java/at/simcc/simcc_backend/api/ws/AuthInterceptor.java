package at.simcc.simcc_backend.api.ws;

import at.simcc.simcc_backend.repo.InfectedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/14/26
 */
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandshakeInterceptor {
    private final InfectedRepository infectedRepository;

    /**
     * Checks whether the {@code iid} was sent in the handshake inside the {@code Authorization}-Header.
     * After that, only {@code attributes.containsKey("authenticated")}, since the handshake will be discarded on an invalid {@code iid}
     */

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        log.info(request.getHeaders().toString());
        if (request.getHeaders().containsHeader("Authorization")) {
            if (request.getHeaders().getFirst("Authorization").startsWith("Bearer ")) {
                try {
                    UUID iid = UUID.fromString(request.getHeaders().getFirst("Authorization").split("Bearer ")[1]);
                    if (infectedRepository.existsInfectedByIid(iid)) {
                        attributes.put("iid", iid);
                        return true;
                    }
                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException _) {}
            }
        }

        response.setStatusCode(HttpStatusCode.valueOf(403));

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
