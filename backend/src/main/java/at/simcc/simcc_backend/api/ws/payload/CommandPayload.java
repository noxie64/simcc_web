package at.simcc.simcc_backend.api.ws.payload;

import lombok.Builder;
import lombok.Data;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/16/26
 */
@Data
public class CommandPayload extends WSAwaitable {
    private String command;

    @Builder
    public CommandPayload(String id, String command) {
        super(id);
        this.command = command;
    }
}