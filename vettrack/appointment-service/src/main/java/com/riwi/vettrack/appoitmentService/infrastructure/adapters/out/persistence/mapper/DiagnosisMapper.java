package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.mapper;

import com.riwi.vettrack.appoitmentService.domain.model.Diagnosis;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.DiagnosisEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiagnosisMapper {

    // Ignoramos la relación 'appointment' aquí, la seteamos en el Adapter
    @Mapping(target = "appointment", ignore = true)
    DiagnosisEntity toEntity(Diagnosis diagnosis);

    // Mapeamos el ID de la cita desde la entidad relacionada hacia el dominio
    @Mapping(target = "appointmentId", source = "appointment.id")
    Diagnosis toDomain(DiagnosisEntity entity);
}