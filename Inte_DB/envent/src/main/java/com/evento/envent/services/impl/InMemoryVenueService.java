package com.evento.envent.services.impl;

import com.evento.envent.controller.dto.CreateVenueRequest;
import com.evento.envent.controller.dto.VenueResponse;
import com.evento.envent.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Profile;
import com.evento.envent.services.VenueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Profile("in-memory")
public class InMemoryVenueService implements VenueService {

    private final Map<Long, VenueResponse> venueStore = new ConcurrentHashMap<>();

    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public List<VenueResponse> findAll() {
        return List.copyOf(venueStore.values());
    }

    @Override
    public VenueResponse findById(Long id) {
        VenueResponse venue = venueStore.get(id);
        if (venue == null) {
            throw new ResourceNotFoundException("Venue no encontrado con ID: " + id);
        }
        return venue;
    }

    @Override
    public VenueResponse createVenue(CreateVenueRequest request) {
        long newId = idCounter.getAndIncrement();

        VenueResponse newVenue = new VenueResponse(
                newId,
                request.getName(),
                request.getCity()
        );

        venueStore.put(newId, newVenue);

        return newVenue;
    }

    @Override
    public VenueResponse updateVenue(Long id, CreateVenueRequest request) {
        VenueResponse existingVenue = this.findById(id);

        VenueResponse updatedVenue = new VenueResponse(
                existingVenue.id(),
                request.getName(),
                request.getCity()
        );

        venueStore.put(id, updatedVenue);

        return updatedVenue;
    }

    @Override
    public void deleteVenue(Long id) {
        this.findById(id);
        venueStore.remove(id);
    }
}