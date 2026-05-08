package at.simcc.simcc_backend.models;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link at.simcc.simcc_backend.entities.Infected}
 */
public record InfectedIdDto(UUID iid) implements Serializable {}