package com.evento.envent.services.impl;

import com.evento.envent.controller.dto.CreateEventRequest;
import com.evento.envent.controller.dto.EventResponse;
import com.evento.envent.entity.EventEntity;
import com.evento.envent.entity.VenueEntity;
import com.evento.envent.exception.DuplicateEventException;
import com.evento.envent.exception.ResourceNotFoundException;
import com.evento.envent.specification.EventSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.evento.envent.repository.EventRepository;
import com.evento.envent.repository.VenueRepository;
import com.evento.envent.services.EventServices;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Eventos basada en JPA.
 * Esta clase SOLO se activará si el perfil de Spring "jpa" está activo.
 */
@Service
@Profile("jpa") // ¡Clave! Este Bean solo existe si spring.profiles.active=jpa
@Transactional // Buena práctica: todas las operaciones de servicio deben ser transaccionales
public class EventServiceImpl implements EventServices {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final EventSpecification eventSpecification;

    // Inyección de dependencias de los repositorios JPA
    public EventServiceImpl(EventRepository eventRepository,
                            VenueRepository venueRepository,
                            EventSpecification eventSpecification) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.eventSpecification = eventSpecification;
    }

    @Override
    @Transactional(readOnly = true) // Optimizacion: esta transacción es solo de lectura
    public List<EventResponse> findAll() {
        return eventRepository.findAll().stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse findById(Long id) {
        EventEntity event = findEventEntityById(id);
        return mapEntityToResponse(event);
    }

    @Override
    public EventResponse createEvent(CreateEventRequest request) {
        if (eventRepository.existsByName(request.getName())) {
            throw new DuplicateEventException("Ya existe un evento con el nombre: " + request.getName());
        }

        VenueEntity venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el Venue con ID: " + request.getVenueId()));

        EventEntity newEvent = new EventEntity();
        newEvent.setName(request.getName());
        newEvent.setCategory(request.getCategory());
        newEvent.setStartDate(request.getStartDate());
        newEvent.setVenue(venue);

        EventEntity savedEvent = eventRepository.save(newEvent);

        return mapEntityToResponse(savedEvent);
    }

    @Override
    public EventResponse updateEvent(Long id, CreateEventRequest request) {
        EventEntity eventToUpdate = findEventEntityById(id);

        VenueEntity venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el Venue con ID: " + request.getVenueId()));


        eventToUpdate.setName(request.getName());
        eventToUpdate.setCategory(request.getCategory());
        eventToUpdate.setStartDate(request.getStartDate());
        eventToUpdate.setVenue(venue);

        EventEntity updatedEvent = eventRepository.save(eventToUpdate);

        return mapEntityToResponse(updatedEvent);
    }

    @Override
    public void deleteEvent(Long id) {
        EventEntity event = findEventEntityById(id);
        eventRepository.delete(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> findPaginated(String city, String category, LocalDateTime startDate, Pageable pageable) {

        // 1. Construir el 'query' dinámico usando la especificación
        Specification<EventEntity> spec = eventSpecification.build(city, category, startDate);

        // 2. Ejecutar la consulta de paginación
        Page<EventEntity> eventPage = eventRepository.findAll(spec, pageable);

        // 3. Mapear la página de Entidades a una Página de DTOs
        return eventPage.map(this::mapEntityToResponse);
    }

    private EventEntity findEventEntityById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con ID: " + id));
    }

    private EventResponse mapEntityToResponse(EventEntity entity) {
        return new EventResponse(
                entity.getId(),
                entity.getName(),
                entity.getCategory(),
                entity.getStartDate(),
                entity.getVenue().getName()
        );
    }
}