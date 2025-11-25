package com.Events.events.infrastructure.mappers;

import com.Events.events.domain.model.User;
import com.Events.events.infrastructure.adapters.out.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    // Convierte entidad JPA -> Dominio
    User toDomain(UserEntity userEntity);

    // Convierte Dominio -> Entidad JPA
    UserEntity toEntity(User user);

    // MapStruct genera la implementación para iterar la lista automáticamente
    List<User> toDomainList(List<UserEntity> userEntities);
}
