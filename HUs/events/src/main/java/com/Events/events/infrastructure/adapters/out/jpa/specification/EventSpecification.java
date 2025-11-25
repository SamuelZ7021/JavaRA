package com.Events.events.infrastructure.adapters.out.jpa.specification;

import com.Events.events.infrastructure.adapters.out.jpa.entity.EventEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class EventSpecification {

    public static Specification<EventEntity> hasCategory(String category){
        return (root, query, criteriaBuilder) -> {
            if(category == null || category.isEmpty()) return null;
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    public static Specification<EventEntity> hasVenueId(Long venueId){
        return (root, query, criteriaBuilder) -> {
            if (venueId == null) return null;
            return criteriaBuilder.equal(root.get("venue").get("id"), venueId);
        };
    }

    public static Specification<EventEntity> hasStatus(String status){
        return (root, query, criteriaBuilder) -> {
            if(status == null || status.isEmpty()) return null;
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<EventEntity> isBetweenDates(LocalDateTime start, LocalDateTime end){
        return (root, query, criteriaBuilder) -> {
            if (start == null || end == null) return null;
            return criteriaBuilder.between(root.get("startDate"), start, end);
        };
    }
}
