package com.Events.events.exception;

public class DuplicateEventException extends RuntimeException {

    public DuplicateEventException(String message) {
        super(message);
    }
}