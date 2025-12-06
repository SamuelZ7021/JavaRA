package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.mapper;

import com.riwi.vettrack.appoitmentService.domain.model.Pet;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.PetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PetMapper {

    PetEntity toEntity(Pet pet);

    Pet toDomain(PetEntity entity);

    List<Pet> toDomainList(List<PetEntity> entities);
}