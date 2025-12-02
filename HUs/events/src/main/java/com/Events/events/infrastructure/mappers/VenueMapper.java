package com.Events.events.infrastructure.mappers;

import com.Events.events.domain.model.Venue;
import com.Events.events.infrastructure.adapters.out.jpa.entity.VenueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VenueMapper {

    Venue toDomain(VenueEntity entity);

    VenueEntity toEntity(Venue domain);

    List<Venue> toDomainList(List<VenueEntity> entities);
}
