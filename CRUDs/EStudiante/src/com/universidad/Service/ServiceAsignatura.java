package com.universidad.Service;

import com.universidad.model.Alumno;
import com.universidad.model.Asignatura;

public class ServiceAsignatura {

    public void agregarAsignatura(Alumno alumno, Asignatura nuevaAsignatura) {
        alumno.getAsignaturas().add(nuevaAsignatura);
    }

    public boolean eliminarAsignatura(Alumno alumno, String nombreAsignatura) {
        return alumno.getAsignaturas().removeIf(asignatura -> asignatura.getAsigNombre().equalsIgnoreCase(nombreAsignatura));
    }

    public double calcularPromedioNotas(Alumno alumno) {
        if (alumno.getAsignaturas().isEmpty()) {
            return 0.0;
        }
        double sumatoriaNotas = 0;
        for (Asignatura asignatura : alumno.getAsignaturas()) {
            sumatoriaNotas += asignatura.getNota();
        }
        return sumatoriaNotas / alumno.getAsignaturas().size();
    }
}
