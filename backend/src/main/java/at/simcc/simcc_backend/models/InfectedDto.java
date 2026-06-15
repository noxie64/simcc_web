package at.simcc.simcc_backend.models;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.net.Inet4Address;
import java.util.UUID;

/**
 * DTO for {@link at.simcc.simcc_backend.entities.Infected}
 */
@Builder
@Value
public class InfectedDto implements Serializable {
    UUID iid;
    String osType;
    String osVersion;
    String osEdition;
    String osCodeName;
    String osBits;
    String osArch;
    Inet4Address currentIpAddress;
    Boolean online;
}