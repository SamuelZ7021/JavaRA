package com.universidad.model;

public class Asignatura {
    private String asigNombre;
    private double nota;

    public Asignatura(String asigNombre, double nota) {
        this.asigNombre = asigNombre;
        this.nota = nota;
    }

    public String getAsigNombre() {
        return asigNombre;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String estado() {
        if (this.nota < 3.5) {
            return "reprueba";
        } else {
            return "aprueba";
        }
    }
}
