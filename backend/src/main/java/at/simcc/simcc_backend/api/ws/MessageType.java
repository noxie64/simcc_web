package at.simcc.simcc_backend.api.ws;

import at.simcc.simcc_backend.api.ws.payload.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/14/26
 */
@Getter
public enum MessageType {
    GOODBYE(StringPayload.class),
    HELLO(StringPayload.class),
    ERR(ERRPayload.class),
    COMMAND(CommandPayload.class),
    SCREENSHOT_RESPONSE(ScreenshotPayload.class),
    SCREENSHOT_REQUEST(null),
    COMMAND_OUTPUT(CommandOutputPayload.class);

    @JsonIgnore
    private final Class<?> type;

    MessageType(Class<?> type) {
        this.type = type;
    }
}
