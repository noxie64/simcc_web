package at.simcc.simcc_backend.models;

import lombok.Data;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.net.Inet4Address;
import java.util.UUID;

/**
 * DTO for {@link at.simcc.simcc_backend.entities.Infected}
 */
@Data
public class InfectedWithLatestIPDto implements Serializable {
    UUID iid;
    String osType;
    String osVersion;
    String osEdition;
    String osCodeName;
    String osBits;
    String osArch;
    String latestIpAddress;
    Boolean online;
}