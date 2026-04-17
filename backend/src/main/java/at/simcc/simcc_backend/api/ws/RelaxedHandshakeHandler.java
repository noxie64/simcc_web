package at.simcc.simcc_backend.api.ws;

import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.io.IOException;
import java.util.List;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/17/26
 */
public class RelaxedHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected void handleInvalidConnectHeader(org.springframework.http.server.ServerHttpRequest request, ServerHttpResponse response) throws IOException {
    }
}
