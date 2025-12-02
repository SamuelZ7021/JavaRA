package com.evento.envent.entity;


import jakarta.persistence.*;

import java.util.Set;
import java.util.HashSet;


@Entity
@Table(name = "venues")
public class VenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(length = 255)
    private String address;


    @OneToMany(mappedBy = "venue",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<EventEntity> events = new HashSet<>(); // Inicializar la colección


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