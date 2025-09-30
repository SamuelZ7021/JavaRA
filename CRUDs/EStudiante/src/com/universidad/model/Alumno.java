package com.universidad.model;

import java.util.ArrayList;
import java.util.List;

public class Alumno extends Persona {
    private List<Asignatura> asignaturas = new ArrayList<>();

    public Alumno(String nombre, int edad) {
        super(nombre, edad);
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }
}
