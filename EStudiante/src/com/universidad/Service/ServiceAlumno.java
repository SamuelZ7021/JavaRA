package com.universidad.Service;

import com.universidad.model.Alumno;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceAlumno {

    private final List<Alumno> alumnos = new ArrayList<>();

    public void agregarAlumno(Alumno alumno) {
        alumnos.add(alumno);
    }

    public boolean eliminarAlumno(int id) {
        return alumnos.removeIf(alumno -> alumno.getId() == id);
    }

    public Optional<Alumno> buscarAlumnoPorId(int id) {
        return alumnos.stream().filter(alumno -> alumno.getId() == id).findFirst();
    }

    public List<Alumno> obtenerTodosLosAlumnos() {
        return new ArrayList<>(alumnos);
    }
}
