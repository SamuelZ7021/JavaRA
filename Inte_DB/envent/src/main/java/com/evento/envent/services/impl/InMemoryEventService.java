package com.evento.envent.services.impl;

import com.evento.envent.controller.dto.CreateEventRequest;
import com.evento.envent.controller.dto.EventResponse;
import com.evento.envent.services.EventServices;
import com.evento.envent.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryEventService implements EventServices {

    // Usamos un Map para simular una base de datos (acceso O(1) por ID)
    private final Map<Long, EventResponse> eventStore = new ConcurrentHashMap<>();

    // Usamos AtomicLong para IDs autoincrementales thread-safe
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public List<EventResponse> findAll() {
        return List.copyOf(eventStore.values());
    }

    @Override
    public EventResponse findById(Long id) {
        EventResponse event = eventStore.get(id);
        if (event == null) {
            throw new ResourceNotFoundException("Evento no encontrado con ID: " + id);
        }
        return event;
    }

    @Override
    public EventResponse createEvent(CreateEventRequest request) {
        long newId = idCounter.getAndIncrement();

        EventResponse newEvent = new EventResponse(
                newId,
                request.getName(),
                request.getCategory(),
                request.getStartDate(),
                request.getVenueName()
        );

        eventStore.put(newId, newEvent);
        return newEvent;
    }

    @Override
    public EventResponse updateEvent(Long id, CreateEventRequest request) {
        EventResponse eventEx = this.findById(id);

        EventResponse eventUpdate = new EventResponse(
                eventEx.id(),
                request.getName(),
                request.getCategory(),
                request.getStartDate(),
                request.getVenueName()
        );
        eventStore.put(eventUpdate.id(), eventUpdate);

        return eventUpdate;
    }

    @Override
    public void deleteEvent(Long id){
        this.findById(id);
        eventStore.remove(id);
    }
}