package com.riwi.vetmock.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailabilityDTO {

    @Data
    public static class Request {
        private Long veterinarioId;
        private LocalDate fecha;
        private LocalTime hora;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Long veterinarioId;
        private boolean disponible;
        private String motivo;
    }
}