package at.simcc.simcc_backend.api.body;

import at.simcc.simcc_backend.api.validation.TrojanBuildConfigConstraint;
import at.simcc.simcc_backend.entities.trojan_setting.TrojanSettingKey;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Map;
import java.util.Objects;

public record TrojanCreationRequest(
        @NotNull
        @Pattern(regexp = "^(\\w| ){1,64}$", message = "Invalid trojan-name!")
        String name,

        @TrojanBuildConfigConstraint
        Map<TrojanSettingKey, Object> buildConfig
) {
}
