package com.evento.envent.services;

import com.evento.envent.controller.dto.CreateVenueRequest;
import com.evento.envent.controller.dto.VenueResponse;
import java.util.List;


public interface VenueService {
    List<VenueResponse> findAll();

    VenueResponse findById(Long id);

    VenueResponse createVenue(CreateVenueRequest request);

    VenueResponse updateVenue(Long id, CreateVenueRequest request);

    void deleteVenue(Long id);
}