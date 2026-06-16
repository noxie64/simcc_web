package at.simcc.simcc_backend.api.ws.payload;

import lombok.Data;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/16/26
 */
@Data
public class CommandOutputPayload extends WSAwaitable {
    private String stdout;

    public CommandOutputPayload(String id, String stdout) {
        super(id);
        this.stdout = stdout;
    }
}
