package com.evento.envent.services;

import com.evento.envent.controller.dto.CreateEventRequest;
import com.evento.envent.controller.dto.EventResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;


public interface EventServices {
    List<EventResponse> findAll();
    EventResponse findById(Long id);
    EventResponse createEvent(CreateEventRequest request);
    EventResponse updateEvent(Long id, CreateEventRequest request);
    void deleteEvent(Long id);
    Page<EventResponse> findPaginated(String city, String category, LocalDateTime startDate, Pageable pageable);
}