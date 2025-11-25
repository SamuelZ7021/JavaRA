package com.Events.events.domain.ports.in;

import com.Events.events.domain.model.Event;

import java.util.List;

public interface EventUseCase {
    Event create(Event event); // O recibir un DTO de entrada si prefieres mapear en el controlador
    Event findById(Long id);
    List<Event> findAll();
    List<Event> findByFilter(int page, int size, String city, String category);
    Event update(Long id, Event event);
    void delete(Long id);
}
