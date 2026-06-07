package at.simcc.simcc_backend.models;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link at.simcc.simcc_backend.entities.Trojan}
 */
@Value
public class TrojanPlainDto implements Serializable {
    UUID ccid;
    String name;
    LocalDateTime createdAt;
}