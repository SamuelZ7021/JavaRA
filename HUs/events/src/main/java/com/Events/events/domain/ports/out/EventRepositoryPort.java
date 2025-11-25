package com.Events.events.domain.ports.out;

import com.Events.events.domain.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepositoryPort {
    Event save(Event event);
    Optional<Event> findById(Long id);
    List<Event> findAll();
    List<Event> findAllPaginated(int page, int size, String city, String category);
    void deleteById(Long id);
    boolean existsByName(String name);

}
