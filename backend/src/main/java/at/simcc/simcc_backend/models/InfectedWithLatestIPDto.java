package at.simcc.simcc_backend.models;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link at.simcc.simcc_backend.entities.Infected}
 */
@Value
public class InfectedWIthLatestIPDto implements Serializable {
    UUID iid;
    String osType;
    String osVersion;
    String osEdition;
    String osCodeName;
    String osBits;
    String osArch;
}