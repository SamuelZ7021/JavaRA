package com.evento.envent.services.impl;

import com.evento.envent.controller.dto.CreateEventRequest;
import com.evento.envent.controller.dto.EventResponse;
import com.evento.envent.controller.dto.VenueResponse;
import com.evento.envent.services.EventServices;
import com.evento.envent.services.VenueService;
import com.evento.envent.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Profile("in-memory")
public class InMemoryEventService implements EventServices {

    private final Map<Long, EventResponse> eventStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    private final VenueService venueService;

    public InMemoryEventService(VenueService venueService) {
        this.venueService = venueService;
    }

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

        VenueResponse venue = venueService.findById(request.getVenueId());
        if (venue == null) {

            throw new ResourceNotFoundException("No se encontró el Venue con ID: " + request.getVenueId());
        }

        long newId = idCounter.getAndIncrement();

        EventResponse newEvent = new EventResponse(
                newId,
                request.getName(),
                request.getCategory(),
                request.getStartDate(),
                venue.name()
        );

        eventStore.put(newId, newEvent);
        return newEvent;
    }

    @Override
    public EventResponse updateEvent(Long id, CreateEventRequest request) {
        EventResponse existingEvent = this.findById(id);

        VenueResponse venue = venueService.findById(request.getVenueId());
        if (venue == null) {
            throw new ResourceNotFoundException("No se encontró el Venue con ID: " + request.getVenueId());
        }

        EventResponse updatedEvent = new EventResponse(
                existingEvent.id(),
                request.getName(),
                request.getCategory(),
                request.getStartDate(),
                venue.name()
        );

        eventStore.put(id, updatedEvent);
        return updatedEvent;
    }

    @Override
    public void deleteEvent(Long id) {
        this.findById(id);
        eventStore.remove(id);
    }

    @Override
    public Page<EventResponse> findPaginated(String city, String category, LocalDateTime startDate, Pageable pageable) {

        // El servicio en memoria NO soporta filtros. Ignoramos los parámetros:
        // city, category, startDate.

        // 1. Obtenemos TODOS los eventos
        List<EventResponse> allEvents = this.findAll();

        // 2. Calculamos la paginación manualmente (simulación)
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allEvents.size());

        List<EventResponse> pageContent;
        if (start > allEvents.size()) {
            pageContent = List.of(); // No hay contenido si el 'offset' es mayor
        } else {
            pageContent = allEvents.subList(start, end);
        }

        // 3. Retornamos un objeto 'Page'
        return new PageImpl<>(pageContent, pageable, allEvents.size());
    }
}