package at.simcc.simcc_backend.api.body;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CCIDRequest(
        @NotNull
        UUID ccid
) {
}
