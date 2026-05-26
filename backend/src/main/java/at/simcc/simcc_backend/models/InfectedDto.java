package at.simcc.simcc_backend.models;

import at.simcc.simcc_backend.entities.OSType;
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
    OSType osType;
    String osSubType;
    String osInfo;
    Inet4Address currentIpAddress;
}