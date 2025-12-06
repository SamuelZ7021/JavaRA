package com.riwi.vettrack.appoitmentService.domain.model;

public class Veterinarian {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Boolean active;

    public Veterinarian(Long id, String name, String email, String phone, Boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.active = active;
    }

    public void deactivete(){
        this.active = false;
    }

    public boolean isActive(){
        return active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Boolean getActive() {
        return active;
    }
}
