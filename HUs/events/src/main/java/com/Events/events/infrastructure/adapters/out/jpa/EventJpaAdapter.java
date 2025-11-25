package com.Events.events.infrastructure.adapters.out.jpa;

import com.Events.events.domain.model.Event;
import com.Events.events.domain.ports.out.EventRepositoryPort;
import com.Events.events.infrastructure.adapters.out.jpa.entity.EventEntity;
import com.Events.events.infrastructure.adapters.out.jpa.specification.EventSpecification;
import com.Events.events.infrastructure.mappers.EventMapper;
import com.Events.events.infrastructure.adapters.out.jpa.repository.EventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class EventJpaAdapter implements EventRepositoryPort {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventJpaAdapter(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public Event save(Event event) {
        EventEntity entity = eventMapper.toEntity(event);
        EventEntity saved = eventRepository.save(entity);
        return eventMapper.toDomain(saved);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id).map(eventMapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return eventMapper.toDomainList(eventRepository.findAll());
    }

    @Override
    public List<Event> findAllPaginated(int page, int size, String city, String category) {
        // 1. Crear el objeto de paginación de Spring
        Pageable pageable = PageRequest.of(page, size);

        List<EventEntity> entities;

        if (city != null || category != null) {
            // Opción A: Crear método en EventRepository (ver paso 4.1)
            entities = eventRepository.findByCityOrCategory(city, category, pageable);
        } else {
            entities = eventRepository.findAll(pageable).getContent();
        }

        return eventMapper.toDomainList(entities);
    }

    public List<Event> findByFilters(Long venueId, String category, String status, LocalDateTime start, LocalDateTime end) {
        Specification<EventEntity> spec = Specification.where(EventSpecification.hasVenueId(venueId))
                .and(EventSpecification.hasCategory(category))
                .and(EventSpecification.hasStatus(status))
                .and(EventSpecification.isBetweenDates(start, end));

        // JpaRepository.findAll(Specification) ejecutará la query optimizada
        return eventMapper.toDomainList(eventRepository.findAll(spec));
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return eventRepository.existsByName(name);
    }
}