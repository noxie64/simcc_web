package at.simcc.simcc_backend.api.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.JsonNode;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/14/26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private MessageType type;
    private JsonNode payload;
}
