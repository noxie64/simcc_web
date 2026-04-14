package at.simcc.simcc_backend.api.ws;

import at.simcc.simcc_backend.repo.InfectedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/14/26
 */
@RequiredArgsConstructor
public class AuthInterceptor implements HandshakeInterceptor {
    private final InfectedRepository infectedRepository;

    /**
     * Checks whether the {@code iid} was sent in the handshake inside the {@code Authorization}-Header.
     * After that, only {@code attributes.containsKey("authenticated")}, since the handshake will be discarded on an invalid {@code iid}
     */

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request.getHeaders().containsHeader("Authorization")) {
            if (request.getHeaders().getFirst("Authorization").startsWith("IID ")) {
                try {
                    Long iid = Long.parseLong(request.getHeaders().getFirst("Authorization").split("IID ")[1]);
                    if (infectedRepository.existsInfectedByIid(iid)) {
                        attributes.put("authenticated", true);
                        return true;
                    }
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    return false;
                }
            }
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
