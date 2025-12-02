package com.Events.events.domain.ports.out;

import com.Events.events.domain.model.Venue;

import java.util.List;
import java.util.Optional;

public interface VenueRepositoryPort {
    Venue save(Venue venue);
    Optional<Venue> findById(Long id);
    List<Venue> findAll();
    void deleteById(Long id);
}
