package at.simcc.simcc_backend.api.body;

import at.simcc.simcc_backend.api.validation.TrojanBuildConfigConstraint;
import at.simcc.simcc_backend.entities.trojan_setting.TrojanSettingKey;

import java.util.Map;
import java.util.Objects;

public record TrojanCreationRequest(
        String name,
        @TrojanBuildConfigConstraint
        Map<TrojanSettingKey, Object> buildConfig
) {
}
