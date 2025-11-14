package com.evento.envent.exception;

import org.springframework.dao.DataIntegrityViolationException; // <-- 1. Importar
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // "Aconseja" a todos los RestControllers
public class GlobalExceptionHandler {

    // Objeto simple para respuestas de error
    private record ErrorResponse(int status, String error, String message, LocalDateTime timestamp) {}

    // MANEJO 404 (Ya existe)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Devuelve 404
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    // MANEJO 400 (Ya existe) - Captura la excepción de @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Devuelve 400
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("timestamp", LocalDateTime.now());

        // Mensajes de error descriptivos
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        response.put("validationErrors", errors); // Un mapa anidado con los fallos

        return response;
    }


    /**
     * Maneja nuestra excepción de negocio custom (Defensa 1).
     * Se lanza desde EventServiceImpl cuando el nombre ya existe.
     */
    @ExceptionHandler(DuplicateEventException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // Devuelve 409
    public ErrorResponse handleDuplicateEvent(DuplicateEventException ex) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(), // Mensaje claro: "Ya existe un evento con el nombre: X"
                LocalDateTime.now()
        );
    }

    /**
     * Maneja la excepción de la capa de base de datos (Defensa 2).
     * Se lanza si violamos un constraint 'UNIQUE' o 'NOT NULL' en la DB.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // Devuelve 409
    public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // El mensaje de 'ex.getMessage()' suele ser muy técnico (ej. "could not execute statement...").
        // Damos un mensaje genérico pero claro.
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "Conflicto de integridad de datos. Es probable que un campo único esté duplicado.",
                LocalDateTime.now()
        );
    }
}