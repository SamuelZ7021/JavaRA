package com.universidad.Service;

import com.universidad.model.Alumno;
import com.universidad.model.Asignatura;

import java.util.List;

public class ServiceAlumno {

    private final AlumnoDAO alumnoDAO;
    private final AsignaturaDAO asignaturaDAO;

    public ServiceAlumno() {
        this.alumnoDAO = new AlumnoDAO();
        this.asignaturaDAO = new AsignaturaDAO();
    }

    public void agregarAlumno(Alumno alumno) {
        if (alumno != null) {
            alumnoDAO.agregarAlumno(alumno);
        }
    }

    public List<Alumno> listarAlumnos() {
        List<Alumno> alumnos = alumnoDAO.obtenerTodosLosAlumnos();
        // Para cada alumno, obtenemos y asignamos sus asignaturas
        for (Alumno alumno : alumnos) {
            List<Asignatura> asignaturas = asignaturaDAO.obtenerAsignaturasDeAlumno(alumno.getId());
            alumno.setAsignaturas(asignaturas);
        }
        return alumnos;
    }

    public void eliminarAlumno(int idAlumno) {
        alumnoDAO.eliminarAlumno(idAlumno);
    }

    public Alumno obtenerAlumnoPorId(int idAlumno) {
        Alumno alumno = alumnoDAO.obtenerAlumnoPorId(idAlumno);
        if (alumno != null) {
            List<Asignatura> asignaturas = asignaturaDAO.obtenerAsignaturasDeAlumno(alumno.getId());
            alumno.setAsignaturas(asignaturas);
        }
        return alumno;
    }
}
