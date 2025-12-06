package com.Events.events.infrastructure.adapters.in.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "{validation.date.range}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
