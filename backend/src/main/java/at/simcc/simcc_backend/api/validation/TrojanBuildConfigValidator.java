package at.simcc.simcc_backend.api.validation;

import at.simcc.simcc_backend.entities.trojan_setting.TrojanSettingKey;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;
import java.util.Objects;

/**
 * Project: simcc_backend
 * Created by: Georg Kollegger
 * Date: 5/25/26
 */
public class TrojanBuildConfigValidator implements
            ConstraintValidator<TrojanBuildConfigConstraint, Map<TrojanSettingKey, Object>> {

    @Override
    public boolean isValid(Map<TrojanSettingKey, Object> values, ConstraintValidatorContext context) {
        if (values == null) return true;

        for (Map.Entry<TrojanSettingKey, Object> entry : values.entrySet()) {
            TrojanSettingKey key = entry.getKey();
            Object value = entry.getValue();

            if (!key.expectedType().isAssignableFrom(value.getClass())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Key '%s' was expected to be of type %s, but got %s!"
                                .formatted(key.name(), key.expectedType().getSimpleName(), value.getClass().getSimpleName())
                );
                return false;
            }
        }

        return true;
    }
}
