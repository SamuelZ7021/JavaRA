package com.universidad.model;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Alumno extends Persona {
    private final List<Asignatura> asignaturas = new ArrayList<>();

    public Alumno(String nombre, int edad) {
        super(nombre, edad);
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    @Override
    public void mostrarInformacion() {
        StringBuilder info = new StringBuilder();
        info.append("ID: ").append(getId()).append("\n");
        info.append("Nombre: ").append(getNombre()).append("\n");
        info.append("Edad: ").append(getEdad()).append("\n");
        info.append("--- Asignaturas ---\n");
        if (asignaturas.isEmpty()) {
            info.append("No tiene asignaturas matriculadas.\n");
        } else {
            for (Asignatura asignatura : asignaturas) {
                info.append("- ").append(asignatura.getAsigNombre()).append(" (Nota: ").append(asignatura.getNota()).append(")\n");
            }
        }
        JOptionPane.showMessageDialog(null, info.toString(), "Información del Alumno", JOptionPane.INFORMATION_MESSAGE);
    }
}
