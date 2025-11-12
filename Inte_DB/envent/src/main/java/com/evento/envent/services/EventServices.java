package com.evento.envent.services;

import com.evento.envent.controller.dto.CreateEventRequest;
import com.evento.envent.controller.dto.EventResponse;
import java.util.List;

public interface EventServices {
    List<EventResponse> findAll();
    EventResponse findById(Long id);
    EventResponse createEvent(CreateEventRequest request);
    EventResponse updateEvent(Long id, CreateEventRequest request);
    void deleteEvent(Long id);
}