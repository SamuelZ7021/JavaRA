package com.Events.events.infrastructure.adapters.in.web.validation;

import com.Events.events.infrastructure.adapters.in.web.dto.resquest.CreateEventRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, CreateEventRequest> {

    @Override
    public boolean isValid(CreateEventRequest request, ConstraintValidatorContext context) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            return true; // Dejar que @NotNull valide los nulos
        }
        return request.getStartDate().isBefore(request.getEndDate());
    }
}