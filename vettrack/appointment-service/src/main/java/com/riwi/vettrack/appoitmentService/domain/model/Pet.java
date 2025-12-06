package com.riwi.vettrack.appoitmentService.domain.model;

import com.riwi.vettrack.appoitmentService.domain.enums.PetStatus;
import com.riwi.vettrack.appoitmentService.domain.enums.Species;
import java.time.LocalDate;

public class Pet {
    private Long id;
    private String name;
    private Species species;
    private String race;
    private Integer age;
    private String ownerName;
    private String ownerDocument;
    private Long ownerId;
    private PetStatus status;

    public Pet() {}

    public Pet(Long id, String name, Species species, String race, Integer age, String ownerName, String ownerDocument, PetStatus status) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.race = race;
        this.age = age;
        this.ownerName = ownerName;
        this.ownerDocument = ownerDocument;
        this.status = status;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Species getSpecies() { return species; }
    public void setSpecies(Species species) { this.species = species; }
    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getOwnerDocument() { return ownerDocument; }
    public void setOwnerDocument(String ownerDocument) { this.ownerDocument = ownerDocument; }
    public PetStatus getStatus() { return status; }
    public void setStatus(PetStatus status) { this.status = status; }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    // Regla de Negocio
    public boolean isActive() {
        return this.status == PetStatus.ACTIVA;
    }

}