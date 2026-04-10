package at.simcc.simcc_backend.models;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link at.simcc.simcc_backend.entities.Infected}
 */
public record InfectedIdDto(Long iid) implements Serializable {}