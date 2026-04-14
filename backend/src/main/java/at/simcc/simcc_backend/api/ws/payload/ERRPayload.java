package at.simcc.simcc_backend.api.ws.payload;

import lombok.Builder;
import lombok.Data;

/**
 * Project: SimCC-Backend
 * Created by: Georg Kollegger
 * Date: 4/14/26
 */
@Data
@Builder
public class ERRPayload {
    private ErrType type;
    private String msg;
}
