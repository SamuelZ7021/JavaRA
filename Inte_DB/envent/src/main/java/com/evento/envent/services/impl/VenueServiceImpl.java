package com.evento.envent.services.impl;

import com.evento.envent.controller.dto.CreateVenueRequest;
import com.evento.envent.controller.dto.VenueResponse;
import com.evento.envent.entity.VenueEntity;
import com.evento.envent.exception.ResourceNotFoundException;
import com.evento.envent.repository.VenueRepository;
import com.evento.envent.services.VenueService;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@Transactional
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    public VenueServiceImpl(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenueResponse> findAll() {
        return venueRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VenueResponse findById(Long id) {
        VenueEntity venue = findVenueEntityById(id);
        return mapEntityToResponse(venue);
    }

    @Override
    public VenueResponse createVenue(CreateVenueRequest request) {
        VenueEntity newVenue = new VenueEntity();
        newVenue.setName(request.getName());
        newVenue.setCity(request.getCity());

        VenueEntity savedVenue = venueRepository.save(newVenue);

        return mapEntityToResponse(savedVenue);
    }

    @Override
    public VenueResponse updateVenue(Long id, CreateVenueRequest request) {
        VenueEntity venueToUpdate = findVenueEntityById(id);

        venueToUpdate.setName(request.getName());
        venueToUpdate.setCity(request.getCity());

        VenueEntity updatedVenue = venueRepository.save(venueToUpdate);

        return mapEntityToResponse(updatedVenue);
    }

    @Override
    public void deleteVenue(Long id) {
        VenueEntity venueToDelete = findVenueEntityById(id);

        venueRepository.delete(venueToDelete);
    }

    private VenueEntity findVenueEntityById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado con ID: " + id));
    }

    private VenueResponse mapEntityToResponse(VenueEntity entity) {
        return new VenueResponse(
                entity.getId(),
                entity.getName(),
                entity.getCity()
        );
    }
}