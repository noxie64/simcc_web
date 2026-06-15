package at.simcc.simcc_backend.models;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link at.simcc.simcc_backend.entities.Infected}
 */
@Value
public class InfectedNoIdDto implements Serializable {
    String osType;
    String osVersion;
    String osEdition;
    String osCodeName;
    String osBits;
    String osArch;
    Boolean online;
}