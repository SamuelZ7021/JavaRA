package com.example1.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND) // Devuelve un código 404
public class ResoucerNotFoundException extends RuntimeException{
    public ResoucerNotFoundException(String message){
        super(message);
    }
}
