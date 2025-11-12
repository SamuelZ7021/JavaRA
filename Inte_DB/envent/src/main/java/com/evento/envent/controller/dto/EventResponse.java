package com.evento.envent.controller.dto;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String name,
        String category,
        LocalDateTime startDate,
        String venueName
) {}
