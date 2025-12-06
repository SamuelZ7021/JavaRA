package com.Events.events.infrastructure.adapters.out.jpa.repository;

import com.Events.events.infrastructure.adapters.out.jpa.entity.EventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    boolean existsByName(String name);

    // Esto hace un LEFT JOIN fetch del venue cuando buscamos eventos
   @Override
   @EntityGraph(attributePaths = {"venue"})
   List<EventEntity> findAll();

   @EntityGraph(attributePaths = {"venue"})
    List<EventEntity> findByVenueId(Long venueId);

    // Consulta JPQL personalizada para filtrar dinámicamente
    @Query("SELECT e FROM EventEntity e " +
            "WHERE (:city IS NULL OR e.venue.city = :city) " +
            "AND (:category IS NULL OR e.category = :category)")
    List<EventEntity> findByCityOrCategory(@Param("city") String city,
                                           @Param("category") String category,
                                           Pageable pageable);
}