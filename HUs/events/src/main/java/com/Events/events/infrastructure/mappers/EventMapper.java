package com.Events.events.infrastructure.mappers;

import com.Events.events.domain.model.Event;
import com.Events.events.infrastructure.adapters.out.jpa.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {VenueMapper.class})
public interface EventMapper {

    Event toDomain(EventEntity entity);

    EventEntity toEntity(Event domain);

    List<Event> toDomainList(List<EventEntity> entities);
}