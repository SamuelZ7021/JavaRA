package com.evento.envent.specification;

import com.evento.envent.entity.EventEntity;
import com.evento.envent.entity.VenueEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventSpecification {

    public Specification<EventEntity> build(String city, String category, LocalDateTime startDate) {

        // Empezamos con una especificación "neutra" que no filtra nada.
        Specification<EventEntity> spec = Specification.where(null);

        // Añadimos filtros condicionalmente (Patrón Builder)
        if (city != null && !city.isBlank()) {
            spec = spec.and(withCity(city));
        }
        if (category != null && !category.isBlank()) {
            spec = spec.and(withCategory(category));
        }
        if (startDate != null) {
            spec = spec.and(withStartDateAfter(startDate));
        }

        return spec;
    }

    /**
     * Retorna una especificación para filtrar por ciudad.
     * ¡Esto requiere un JOIN!
     */
    private Specification<EventEntity> withCity(String city) {
        return (root, query, criteriaBuilder) -> {
            // 1. Hacemos un JOIN desde Event (root) hasta Venue ("venue")
            // Esto se traduce a: "FROM events e JOIN venues v ON e.venue_id = v.id"
            Join<EventEntity, VenueEntity> venueJoin = root.join("venue");

            // 2. Comparamos el campo "city" en la entidad Venue
            // Esto se traduce a: "WHERE v.city = ?"
            return criteriaBuilder.equal(venueJoin.get("city"), city);
        };
    }

    /**
     * Retorna una especificación para filtrar por categoría (campo simple).
     */
    private Specification<EventEntity> withCategory(String category) {
        // (root, query, criteriaBuilder) es una expresión lambda
        return (root, query, criteriaBuilder) ->
                // Esto se traduce a: "WHERE e.category = ?"
                criteriaBuilder.equal(root.get("category"), category);
    }

    /**
     * Retorna una especificación para filtrar por fecha de inicio.
     */
    private Specification<EventEntity> withStartDateAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) ->
                // Esto se traduce a: "WHERE e.start_date >= ?"
                criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), date);
    }
}