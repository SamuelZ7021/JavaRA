package com.Events.events.infrastructure.adapters.out.jpa.repository;

import com.Events.events.infrastructure.adapters.out.jpa.entity.EventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    // Necesario para la validación de duplicados en el caso de uso
    boolean existsByName(String name);

    // Consulta JPQL personalizada para filtrar dinámicamente
    @Query("SELECT e FROM EventEntity e " +
            "WHERE (:city IS NULL OR e.venue.city = :city) " +
            "AND (:category IS NULL OR e.category = :category)")
    List<EventEntity> findByCityOrCategory(@Param("city") String city,
                                           @Param("category") String category,
                                           Pageable pageable);
}