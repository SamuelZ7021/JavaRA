package com.Events.events.application.usecase;

import com.Events.events.domain.model.Venue;
import com.Events.events.domain.ports.in.VenueUseCase;
import com.Events.events.domain.ports.out.VenueRepositoryPort;
import com.Events.events.exception.ResourceNotFoundException;

import java.util.List;

public class VenueUseCaseImpl implements VenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public VenueUseCaseImpl(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public Venue create(Venue venue) {
        return venueRepositoryPort.save(venue);
    }

    @Override
    public Venue findById(Long id) {
        return venueRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado con ID: " + id));
    }

    @Override
    public List<Venue> findAll() {
        return venueRepositoryPort.findAll();
    }

    @Override
    public Venue update(Long id, Venue venue) {
        Venue existingVenue = findById(id);

        existingVenue.setName(venue.getName());
        existingVenue.setCity(venue.getCity());

        return venueRepositoryPort.save(existingVenue);
    }

    @Override
    public void delete(Long id) {
        findById(id); // Validar
        venueRepositoryPort.deleteById(id);
    }
}