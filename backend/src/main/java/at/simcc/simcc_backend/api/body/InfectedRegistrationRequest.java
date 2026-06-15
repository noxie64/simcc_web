package at.simcc.simcc_backend.api.body;

import at.simcc.simcc_backend.entities.Trojan;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InfectedRegistrationRequest(
        @NotNull
        UUID ccid,
        String osType,
        String osVersion,
        String osEdition,
        String osCodeName,
        String osBits,
        String osArch
) {
}
