package at.simcc.simcc_backend.models;

import at.simcc.simcc_backend.entities.Trojan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * DTO for {@link Trojan}
 */
@Data
@AllArgsConstructor
public class TrojanDisplayDto implements Serializable {
    private UUID ccid;
    private String name;
    private LocalDateTime lastBuilt;
    private Boolean building;
}