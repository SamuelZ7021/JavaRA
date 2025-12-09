package com.coopcredit.creditapplication.application.mapper;

import com.coopcredit.creditapplication.domain.model.Role;
import com.coopcredit.creditapplication.domain.model.User;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(User domain);
    User toDomain(UserEntity entity);
    @ValueMapping(source = "ROLE_AFILIADO", target = "ROLE_AFILIADO")
    @ValueMapping(source = "ROLE_ANALISTA", target = "ROLE_ANALISTA")
    @ValueMapping(source = "ROLE_ADMIN", target = "ROLE_ADMIN")
    UserEntity.Role mapToEntityRole(Role role);
    @InheritInverseConfiguration
    Role mapToDomainRole(UserEntity.Role role);
}