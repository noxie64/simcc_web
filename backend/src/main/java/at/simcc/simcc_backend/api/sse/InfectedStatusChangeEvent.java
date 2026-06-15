package at.simcc.simcc_backend.api.sse;

import java.util.UUID;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/15/26
 */
public record InfectedStatusChangeEvent(UUID iid, boolean online) {
}
