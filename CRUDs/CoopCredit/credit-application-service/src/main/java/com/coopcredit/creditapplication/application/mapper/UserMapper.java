package com.coopcredit.creditapplication.application.mapper;

import com.coopcredit.creditapplication.domain.model.Role;
import com.coopcredit.creditapplication.domain.model.User;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // 1. Quitamos la línea de @Mapping(target="authorities", ignore=true)
    // El Builder de Lombok no tiene ese campo, así que no hace falta ignorarlo.
    UserEntity toEntity(User domain);

    User toDomain(UserEntity entity);

    // 2. Agregamos traductores explícitos para los Enums
    // Domain (AFILIADO) -> Entity (ROLE_AFILIADO)
    @ValueMapping(source = "AFILIADO", target = "ROLE_AFILIADO")
    @ValueMapping(source = "ANALISTA", target = "ROLE_ANALISTA")
    @ValueMapping(source = "ADMIN", target = "ROLE_ADMIN")
    UserEntity.Role mapToEntityRole(Role role);

    // Entity (ROLE_AFILIADO) -> Domain (AFILIADO)
    @InheritInverseConfiguration
    Role mapToDomainRole(UserEntity.Role role);
}