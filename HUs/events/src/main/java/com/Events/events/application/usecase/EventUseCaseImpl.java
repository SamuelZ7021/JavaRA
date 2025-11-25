package com.Events.events.application.usecase;

import com.Events.events.domain.model.Event;
import com.Events.events.domain.model.Venue;
import com.Events.events.domain.ports.in.EventUseCase;
import com.Events.events.domain.ports.out.EventRepositoryPort;
import com.Events.events.domain.ports.out.VenueRepositoryPort;
import com.Events.events.exception.DuplicateEventException; // Reusamos tus excepciones
import com.Events.events.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventUseCaseImpl implements EventUseCase {

    private final EventRepositoryPort eventRepositoryPort;
    private final VenueRepositoryPort venueRepositoryPort;


    @Override
    @Transactional
    public Event create(Event event) {
        // 1. Regla de Negocio: Validar nombre duplicado
        if (eventRepositoryPort.existsByName(event.getName())) {
            throw new DuplicateEventException("Ya existe un evento con el nombre: " + event.getName());
        }

        // 2. Validar que el Venue existe (El objeto event trae un Venue "parcial" solo con ID, necesitamos el real)
        Long venueId = event.getVenue().getId();
        Venue fullVenue = venueRepositoryPort.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el Venue con ID: " + venueId));

        // 3. Asignar el Venue completo al evento
        event.setVenue(fullVenue);

        // 4. Guardar
        return eventRepositoryPort.save(event);
    }

    @Override
    public Event findById(Long id) {
        return eventRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return eventRepositoryPort.findAll();
    }

    @Override
    public List<Event> findByFilter(int page, int size, String city, String category) {
        // Aquí podrías validar que 'page' no sea negativo, etc.
        return eventRepositoryPort.findAllPaginated(page, size, city, category);
    }

    @Override
    @Transactional
    public Event update(Long id, Event event) {
        // 1. Buscar existente
        Event existingEvent = findById(id);

        // 2. Buscar Venue (si cambió)
        Long venueId = event.getVenue().getId();
        Venue fullVenue = venueRepositoryPort.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el Venue con ID: " + venueId));

        // 3. Actualizar campos
        existingEvent.setName(event.getName());
        existingEvent.setCategory(event.getCategory());
        existingEvent.setStartDate(event.getStartDate());
        existingEvent.setVenue(fullVenue);

        // 4. Guardar
        return eventRepositoryPort.save(existingEvent);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findById(id); // Validar existencia
        eventRepositoryPort.deleteById(id);
    }
}