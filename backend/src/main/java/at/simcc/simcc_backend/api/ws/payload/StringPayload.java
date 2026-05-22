package at.simcc.simcc_backend.api.ws.payload;

import lombok.Builder;
import lombok.Data;

/**
 * Project: simcc_backend
 * Created by: Georg Kollegger
 * Date: 5/22/26
 */
@Builder
@Data
public class StringPayload {
    private String content;
}

