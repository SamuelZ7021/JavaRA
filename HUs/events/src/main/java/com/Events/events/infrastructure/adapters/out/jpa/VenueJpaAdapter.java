package com.Events.events.infrastructure.adapters.out.jpa;

import com.Events.events.domain.model.Venue;
import com.Events.events.domain.ports.out.VenueRepositoryPort;
import com.Events.events.infrastructure.adapters.out.jpa.entity.VenueEntity;
import com.Events.events.infrastructure.mappers.VenueMapper;
import com.Events.events.infrastructure.adapters.out.jpa.repository.VenueRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class VenueJpaAdapter implements VenueRepositoryPort {
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public VenueJpaAdapter(VenueRepository venueRepository, VenueMapper venueMapper) {
        this.venueRepository = venueRepository;
        this.venueMapper = venueMapper;
    }

    @Override
    public Venue save(Venue venue) {
        VenueEntity entity = venueMapper.toEntity(venue);
        VenueEntity saved = venueRepository.save(entity);
        return venueMapper.toDomain(saved);
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return venueRepository.findById(id).map(venueMapper::toDomain);
    }

    @Override
    public List<Venue> findAll() {
        return venueMapper.toDomainList(venueRepository.findAll());
    }

    @Override
    public void deleteById(Long id) {
        venueRepository.deleteById(id);
    }
}