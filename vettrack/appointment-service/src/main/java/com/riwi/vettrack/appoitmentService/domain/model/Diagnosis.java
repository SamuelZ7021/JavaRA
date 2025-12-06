package com.riwi.vettrack.appoitmentService.domain.model;

public class Diagnosis {
    private Long id;
    private String description;
    private String  treatment;
    private String recommendations;
    private Long appointmentId;

    public Diagnosis() {}

    public Diagnosis(Long id, String description, String treatment, String recommendations, Long appointmentId) {
        this.id = id;
        this.description = description;
        this.treatment = treatment;
        this.recommendations = recommendations;
        this.appointmentId = appointmentId;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getTreatment() {
        return treatment;
    }
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getRecommendations() {
        return recommendations;
    }
    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
}
