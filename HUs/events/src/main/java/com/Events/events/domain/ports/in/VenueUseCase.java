package com.Events.events.domain.ports.in;

import com.Events.events.domain.model.Venue;

import java.util.List;

public interface VenueUseCase {
    Venue create(Venue venue);
    Venue findById(Long id);
    List<Venue> findAll();
    Venue update(Long id, Venue venue);
    void delete(Long id);
}
