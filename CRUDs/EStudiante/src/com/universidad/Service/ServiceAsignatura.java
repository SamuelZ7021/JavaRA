package com.universidad.Service;

import com.universidad.model.Asignatura;

public class ServiceAsignatura {

    private final AsignaturaDAO asignaturaDAO;

    public ServiceAsignatura() {
        this.asignaturaDAO = new AsignaturaDAO();
    }

    public void agregarAsignaturaAAlumno(Asignatura asignatura, int idAlumno) {
        if (asignatura != null && idAlumno > 0) {
            asignaturaDAO.agregarAsignaturaAAlumno(asignatura, idAlumno);
        }
    }

    public void eliminarAsignatura(int idAsignatura) {
        asignaturaDAO.eliminarAsignatura(idAsignatura);
    }
}
