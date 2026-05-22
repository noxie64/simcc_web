package at.simcc.simcc_backend.api.ws;

import at.simcc.simcc_backend.api.ws.payload.ERRPayload;
import at.simcc.simcc_backend.api.ws.payload.ErrType;
import at.simcc.simcc_backend.api.ws.payload.StringPayload;
import at.simcc.simcc_backend.repo.InfectedRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/14/26
 */
@RequiredArgsConstructor
@Component
public class WebsocketHandler extends AbstractWebSocketHandler {

    private final ObjectMapper objectMapper = JsonMapper.builder()
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                .build();
    private final Set<WebSocketSession> sessions =
            ConcurrentHashMap.newKeySet();

    private final InfectedRepository infectedRepository;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        try {
            Message msg = objectMapper.readValue(message.getPayload(), Message.class);

            switch (msg.getType()) {
                case HELLO -> {
                    StringPayload helloPayload = objectMapper.treeToValue(msg.getPayload(), StringPayload.class);

                    sendMessage(session,
                            Message.builder()
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
                }
            }
        } catch (JacksonException e) {
            sendError(
                    session,
                    ERRPayload.builder()
                            .type(ErrType.INV_REQ)
                            .build()
            );
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    private void sendMessage(WebSocketSession session, Message message) throws IOException {
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
    }

    private void sendError(WebSocketSession session, ERRPayload errPayload) throws IOException {
        session.sendMessage(new TextMessage(
                objectMapper.writeValueAsString(
                        Message.builder()
                                .type(MessageType.ERR)
                                .payload(
                                        objectMapper.valueToTree(errPayload)
                                )
                                .build()
                )
        ));
    }

}
