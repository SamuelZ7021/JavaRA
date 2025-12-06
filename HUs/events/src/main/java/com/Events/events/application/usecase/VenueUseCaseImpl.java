package com.Events.events.application.usecase;

import com.Events.events.domain.model.Venue;
import com.Events.events.domain.ports.in.VenueUseCase;
import com.Events.events.domain.ports.out.VenueRepositoryPort;
import com.Events.events.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class VenueUseCaseImpl implements VenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    @Override
    @Transactional
    public Venue create(Venue venue) {
        return venueRepositoryPort.save(venue);
    }

    @Override
    public Venue findById(Long id) {
        return venueRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venue> findAll() {
        return venueRepositoryPort.findAll();
    }

    @Override
    @Transactional
    public Venue update(Long id, Venue venue) {
        Venue existingVenue = findById(id);

        existingVenue.setName(venue.getName());
        existingVenue.setCity(venue.getCity());

        return venueRepositoryPort.save(existingVenue);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findById(id); // Validar
        venueRepositoryPort.deleteById(id);
    }
}