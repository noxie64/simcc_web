package at.simcc.simcc_backend.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Project: simcc_backend
 * Created by: Georg Kollegger
 * Date: 5/25/26
 */
@Documented
@Constraint(validatedBy = TrojanBuildConfigValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TrojanBuildConfigConstraint {
    String message() default "Value type was not valid!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}