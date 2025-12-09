package com.coopcredit.creditapplication.infrastructure.adapter.input.rest.advice;

import com.coopcredit.creditapplication.domain.exception.BusinessException;
import com.coopcredit.creditapplication.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerAdvice {

    // 1. Manejo de errores de validación (@Valid, @NotNull, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed for one or more fields.");
        problem.setTitle("Input Validation Error");
        problem.setType(URI.create("https://coopcredit.com/errors/validation"));
        problem.setProperty("timestamp", Instant.now());

        // Recolectamos campo por campo cuál fue el error
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Agregamos el mapa de errores al JSON de respuesta
        problem.setProperty("errors", errors);

        return problem;
    }

    // 2. Errores de Negocio (Lógica custom)
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problem.setTitle("Business Rule Violation");
        problem.setType(URI.create("https://coopcredit.com/errors/business-rule"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // 3. Recursos no encontrados (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFoundException(ResourceNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problem.setTitle("Resource Not Found");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}