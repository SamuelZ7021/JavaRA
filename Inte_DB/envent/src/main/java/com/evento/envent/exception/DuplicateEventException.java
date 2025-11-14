package com.evento.envent.exception;

public class DuplicateEventException extends RuntimeException {

    public DuplicateEventException(String message) {
        super(message);
    }
}