package com.Events.events.infrastructure.adapters.in.web.dto.response;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String name,
        String category,
        LocalDateTime startDate,
        String venueName
) {}
