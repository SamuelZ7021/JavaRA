package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.mapper;

import com.riwi.vettrack.appoitmentService.domain.model.Appointment;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.AppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {VeterinarianMapper.class}, // <--- CLAVE: Usa el mapper del veterinario
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AppointmentMapper {

    // Entity -> Domain
    // Al usar 'uses', MapStruct sabe convertir automáticamente VeterinarianEntity -> Veterinarian
    Appointment toDomain(AppointmentEntity entity);

    // Domain -> Entity
    AppointmentEntity toEntity(Appointment domain);

    List<Appointment> toDomainList(List<AppointmentEntity> entities);
}