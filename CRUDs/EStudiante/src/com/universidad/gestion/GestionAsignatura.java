package com.universidad.gestion;

import com.universidad.Service.ServiceAlumno;
import com.universidad.Service.ServiceAsignatura;
import com.universidad.model.Asignatura;
import com.universidad.util.Validaciones;

import javax.swing.*;

public class GestionAsignatura {
    private final ServiceAlumno serviceAlumno;
    private final ServiceAsignatura serviceAsignatura;

    public GestionAsignatura(ServiceAlumno serviceAlumno, ServiceAsignatura serviceAsignatura) {
        this.serviceAlumno = serviceAlumno;
        this.serviceAsignatura = serviceAsignatura;
    }

    public void agregarAsignatura() {
        try {
            String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID del alumno:", "Agregar Asignatura", JOptionPane.PLAIN_MESSAGE);
            if (idStr == null) return; // Cancelado
            int idAlumno = Integer.parseInt(idStr);

            serviceAlumno.buscarAlumnoPorId(idAlumno).ifPresentOrElse(alumno -> {
                String nombreAsignatura = JOptionPane.showInputDialog(null, "Ingrese el nombre de la asignatura:", "Agregar Asignatura", JOptionPane.PLAIN_MESSAGE);
                if (!Validaciones.esTextoValido(nombreAsignatura)) {
                    JOptionPane.showMessageDialog(null, "El nombre de la asignatura no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String notaStr = JOptionPane.showInputDialog(null, "Ingrese la nota de la asignatura (0.0 - 5.0):", "Agregar Asignatura", JOptionPane.PLAIN_MESSAGE);
                    if (notaStr == null) return; // Cancelado
                    double nota = Double.parseDouble(notaStr);

                    if (!Validaciones.esNotaValida(nota)) {
                        JOptionPane.showMessageDialog(null, "La nota debe estar entre 0.0 y 5.0.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Asignatura nuevaAsignatura = new Asignatura(nombreAsignatura, nota);
                    serviceAsignatura.agregarAsignatura(alumno, nuevaAsignatura);
                    JOptionPane.showMessageDialog(null, "Asignatura '" + nombreAsignatura + "' agregada correctamente al alumno " + alumno.getNombre() + ".", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "La nota debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                }

            }, () -> {
                JOptionPane.showMessageDialog(null, "No se encontró ningún alumno con el ID " + idAlumno + ".", "Error", JOptionPane.ERROR_MESSAGE);
            });

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarAsignatura() {
        try {
            String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID del alumno:", "Eliminar Asignatura", JOptionPane.PLAIN_MESSAGE);
            if (idStr == null) return; // Cancelado
            int idAlumno = Integer.parseInt(idStr);

            serviceAlumno.buscarAlumnoPorId(idAlumno).ifPresentOrElse(alumno -> {
                String nombreAsignatura = JOptionPane.showInputDialog(null, "Ingrese el nombre de la asignatura a eliminar:", "Eliminar Asignatura", JOptionPane.PLAIN_MESSAGE);
                if (!Validaciones.esTextoValido(nombreAsignatura)) {
                    JOptionPane.showMessageDialog(null, "El nombre de la asignatura no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (serviceAsignatura.eliminarAsignatura(alumno, nombreAsignatura)) {
                    JOptionPane.showMessageDialog(null, "Asignatura '" + nombreAsignatura + "' eliminada correctamente del alumno " + alumno.getNombre() + ".", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "El alumno " + alumno.getNombre() + " no tiene ninguna asignatura llamada '" + nombreAsignatura + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }, () -> {
                JOptionPane.showMessageDialog(null, "No se encontró ningún alumno con el ID " + idAlumno + ".", "Error", JOptionPane.ERROR_MESSAGE);
            });

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}
