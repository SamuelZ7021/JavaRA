package com.example1.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Este combina el ControllerAdvice y el ResponseBody
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Devuelve un código 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    // Iniciamos el metodo con el Map para que recorra los errores
    public Map<String, String> handlerMethodArgumentNotValid(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) // Devuelve un código 404
    @ExceptionHandler(ResoucerNotFoundException.class)
    public Map<String, String> handlerResoucerNotFoundException(ResoucerNotFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED) // Devuelve un código 401
    @ExceptionHandler(BadCredentialsException.class)
    public Map<String, String> handleBadCredentialsException(BadCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Credenciales incorrectas. Por favor, verifique su usuario y contraseña.");
        return error;
    }

    // ... (dentro de GlobalExceptionHandler)

    // Manejador para usuario ya existente (Error 409)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public Map<String, String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }
}
