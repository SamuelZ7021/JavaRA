package com.universidad.app;

import com.universidad.Service.ServiceAlumno;
import com.universidad.Service.ServiceAsignatura;
import com.universidad.model.Alumno;
import com.universidad.model.Asignatura;

import javax.swing.*;
import java.util.List;

public class Menu {
    private final ServiceAlumno serviceAlumno = new ServiceAlumno();
    private final ServiceAsignatura serviceAsignatura = new ServiceAsignatura();

    public void mostrarMenu() {
        String opcion;
        do {
            String menuTexto = "--- Menú de Gestión ---\n" +
                    "1. Agregar nuevo alumno\n" +
                    "2. Listar todos los alumnos\n" +
                    "3. Mostrar información de un alumno\n" +
                    "4. Agregar asignatura a alumno\n" +
                    "5. Eliminar asignatura de alumno\n" +
                    "6. Eliminar alumno\n" +
                    "7. Salir";

            opcion = JOptionPane.showInputDialog(null, menuTexto, "Menú Principal", JOptionPane.PLAIN_MESSAGE);

            if (opcion == null) { // Si el usuario cierra la ventana
                opcion = "7";
            }

            try {
                switch (opcion) {
                    case "1":
                        agregarNuevoAlumno();
                        break;
                    case "2":
                        listarAlumnos();
                        break;
                    case "3":
                        mostrarInformacionDeAlumno();
                        break;
                    case "4":
                        agregarAsignaturaAAlumno();
                        break;
                    case "5":
                        eliminarAsignaturaDeAlumno();
                        break;
                    case "6":
                        eliminarAlumno();
                        break;
                    case "7":
                        JOptionPane.showMessageDialog(null, "Saliendo del programa...");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opción no válida. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } while (!opcion.equals("7"));
    }

    private void agregarNuevoAlumno() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del alumno:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        int edad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la edad del alumno:"));
        Alumno alumno = new Alumno(nombre, edad);
        serviceAlumno.agregarAlumno(alumno);
        JOptionPane.showMessageDialog(null, "Alumno agregado exitosamente con ID: " + alumno.getId());
    }

    private void listarAlumnos() {
        List<Alumno> alumnos = serviceAlumno.listarAlumnos();
        StringBuilder sb = new StringBuilder("--- Lista de Alumnos ---\n");
        if (alumnos.isEmpty()) {
            sb.append("No hay alumnos registrados.");
        } else {
            for (Alumno al : alumnos) {
                sb.append("ID: ").append(al.getId()).append(", Nombre: ").append(al.getNombre()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void mostrarInformacionDeAlumno() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del alumno:"));
        Alumno alumno = serviceAlumno.obtenerAlumnoPorId(id);
        if (alumno == null) {
            JOptionPane.showMessageDialog(null, "Alumno no encontrado.");
            return;
        }

        StringBuilder info = new StringBuilder();
        info.append("ID: ").append(alumno.getId()).append("\n");
        info.append("Nombre: ").append(alumno.getNombre()).append("\n");
        info.append("Edad: ").append(alumno.getEdad()).append("\n");
        info.append("--- Asignaturas ---\n");
        if (alumno.getAsignaturas().isEmpty()) {
            info.append("No tiene asignaturas matriculadas.\n");
        } else {
            for (Asignatura asig : alumno.getAsignaturas()) {
                info.append("- ID: ").append(asig.getId()).append(", Nombre: ").append(asig.getAsigNombre()).append(", Nota: ").append(asig.getNota()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, info.toString(), "Información del Alumno", JOptionPane.INFORMATION_MESSAGE);
    }

    private void agregarAsignaturaAAlumno() {
        int idAlumno = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del alumno al que desea agregar la asignatura:"));
        if (serviceAlumno.obtenerAlumnoPorId(idAlumno) == null) {
            JOptionPane.showMessageDialog(null, "Alumno no encontrado.");
            return;
        }

        String nombreAsig = JOptionPane.showInputDialog("Ingrese el nombre de la asignatura:");
        double nota = Double.parseDouble(JOptionPane.showInputDialog("Ingrese la nota de la asignatura:"));

        Asignatura asignatura = new Asignatura(nombreAsig, nota);
        serviceAsignatura.agregarAsignaturaAAlumno(asignatura, idAlumno);
        JOptionPane.showMessageDialog(null, "Asignatura agregada exitosamente.");
    }

    private void eliminarAsignaturaDeAlumno() {
        int idAlumno = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del alumno para ver sus asignaturas:"));
        Alumno alumno = serviceAlumno.obtenerAlumnoPorId(idAlumno);
        if (alumno == null || alumno.getAsignaturas().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El alumno no existe o no tiene asignaturas.");
            return;
        }

        StringBuilder sb = new StringBuilder("Asignaturas del alumno:\n");
        for (Asignatura asig : alumno.getAsignaturas()) {
            sb.append("ID: ").append(asig.getId()).append(", Nombre: ").append(asig.getAsigNombre()).append("\n");
        }
        sb.append("\nIngrese el ID de la asignatura a eliminar:");

        int idAsignatura = Integer.parseInt(JOptionPane.showInputDialog(sb.toString()));
        serviceAsignatura.eliminarAsignatura(idAsignatura);
        JOptionPane.showMessageDialog(null, "Asignatura eliminada (si existía).");
    }

    private void eliminarAlumno() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del alumno a eliminar:"));
        serviceAlumno.eliminarAlumno(id);
        JOptionPane.showMessageDialog(null, "Alumno eliminado exitosamente.");
    }
}
