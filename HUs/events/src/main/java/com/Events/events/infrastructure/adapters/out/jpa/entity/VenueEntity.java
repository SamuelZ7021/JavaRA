package com.Events.events.infrastructure.adapters.out.jpa.entity;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "venues")
public class VenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // CRÍTICO: Usar IDENTITY para MySQL
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(length = 255)
    private String address;

    @OneToMany(mappedBy = "venue",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<EventEntity> events = new HashSet<>();

    public void addEvent(EventEntity event){
        events.add(event);
        event.setVenue(this);
    }

    public void removeEvent(EventEntity event){
        events.remove(event);
        event.setVenue(null);
    }

    public VenueEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Set<EventEntity> getEvents() { return events; }
    public void setEvents(Set<EventEntity> events) { this.events = events; }
}