package com.universidad.gestion;

import com.universidad.Service.ServiceAlumno;
import com.universidad.model.Alumno;
import com.universidad.util.Validaciones;

import javax.swing.*;
import java.util.List;

public class GestionAlumno {
    private final ServiceAlumno serviceAlumno;

    public GestionAlumno(ServiceAlumno serviceAlumno) {
        this.serviceAlumno = serviceAlumno;
    }

    public void agregarNuevoAlumno() {
        String nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre del alumno:", "Nuevo Alumno", JOptionPane.PLAIN_MESSAGE);
        if (!Validaciones.esTextoValido(nombre)) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String edadStr = JOptionPane.showInputDialog(null, "Ingrese la edad del alumno:", "Nuevo Alumno", JOptionPane.PLAIN_MESSAGE);
            if (edadStr == null) return; // El usuario canceló
            int edad = Integer.parseInt(edadStr);

            if (!Validaciones.esEdadValida(edad)) {
                JOptionPane.showMessageDialog(null, "La edad no puede ser un número negativo.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Alumno nuevoAlumno = new Alumno(nombre, edad);
            serviceAlumno.agregarAlumno(nuevoAlumno);
            JOptionPane.showMessageDialog(null, "Alumno '" + nombre + "' agregado con éxito con el ID: " + nuevoAlumno.getId(), "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La edad debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarAlumno() {
        try {
            String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID del alumno a eliminar:", "Eliminar Alumno", JOptionPane.PLAIN_MESSAGE);
            if (idStr == null) return; // El usuario canceló
            int id = Integer.parseInt(idStr);

            if (serviceAlumno.eliminarAlumno(id)) {
                JOptionPane.showMessageDialog(null, "Alumno con ID " + id + " eliminado correctamente.", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún alumno con el ID " + id + ".", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarInformacion() {
        try {
            String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID del alumno para ver su información:", "Mostrar Información", JOptionPane.PLAIN_MESSAGE);
            if (idStr == null) return; // Cancelado
            int idAlumno = Integer.parseInt(idStr);

            serviceAlumno.buscarAlumnoPorId(idAlumno).ifPresentOrElse(
                    alumno -> alumno.mostrarInformacion(),
                    () -> JOptionPane.showMessageDialog(null, "No se encontró ningún alumno con el ID " + idAlumno + ".", "Error", JOptionPane.ERROR_MESSAGE)
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void listarAlumnos() {
        List<Alumno> alumnos = serviceAlumno.obtenerTodosLosAlumnos();
        if (alumnos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay alumnos registrados.", "Lista de Alumnos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder lista = new StringBuilder("--- Lista de Alumnos Registrados ---\n");
        for (Alumno alumno : alumnos) {
            lista.append("ID: \n").append(alumno.getId())
                 .append(", Nombre: \n").append(alumno.getNombre())
                 .append(", Edad: ").append(alumno.getEdad())
                 .append("\n");
        }

        JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea(lista.toString())), "Lista de Alumnos", JOptionPane.PLAIN_MESSAGE);
    }
}
