package com.Events.events.infrastructure.adapters.out.jpa.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events",
        uniqueConstraints = @UniqueConstraint(name = "uq_event_name", columnNames = "name"),
        indexes = {
                @Index(name = "idx_event_venue", columnList = "venue_id"),
                @Index(name = "idx_event_date", columnList = "start_date")
        }
)
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // CRÍTICO: Usar IDENTITY para MySQL
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false) // Agregamos endDate para consistencia con DTOs
    private LocalDateTime endDate;

    @Column(length = 20)
    private String status = "ACTIVE";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private VenueEntity venue;

    public EventEntity() {
    }

    // Getters y Setters manuales (o podrías usar Lombok @Data si prefieres)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public VenueEntity getVenue() { return venue; }
    public void setVenue(VenueEntity venue) { this.venue = venue; }
}