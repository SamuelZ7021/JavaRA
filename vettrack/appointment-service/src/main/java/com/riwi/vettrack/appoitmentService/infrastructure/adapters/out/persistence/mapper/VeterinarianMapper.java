package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.mapper;


import com.riwi.vettrack.appoitmentService.domain.model.Veterinarian;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.VeterinarianEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface VeterinarianMapper {
    Veterinarian toDomain(VeterinarianEntity entity);

    VeterinarianEntity toEntity(Veterinarian domain);

}
