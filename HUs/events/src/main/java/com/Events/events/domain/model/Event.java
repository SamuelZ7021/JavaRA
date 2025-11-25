package com.Events.events.domain.model;

import java.time.LocalDateTime;

public class Event {
    private Long id;
    private String name;
    private String category;
    private LocalDateTime startDate;
    private Venue venue;

    public Event(Long id, String name, String category, LocalDateTime startDate, Venue venue) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.startDate = startDate;
        this.venue = venue;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
}