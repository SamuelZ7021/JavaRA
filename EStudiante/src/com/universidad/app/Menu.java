package com.universidad.app;

import com.universidad.Service.ServiceAlumno;
import com.universidad.Service.ServiceAsignatura;
import com.universidad.gestion.GestionAlumno;
import com.universidad.gestion.GestionAsignatura;

import javax.swing.*;

public class Menu {
    // Los servicios que manejan la lógica de negocio (datos)
    private final ServiceAlumno serviceAlumno = new ServiceAlumno();
    private final ServiceAsignatura serviceAsignatura = new ServiceAsignatura();

    // Las clases que manejan la lógica de la interfaz de usuario (JOptionPane)
    private final GestionAlumno gestionAlumno = new GestionAlumno(serviceAlumno);
    private final GestionAsignatura gestionAsignatura = new GestionAsignatura(serviceAlumno, serviceAsignatura);

    public void mostrarMenu() {
        String opcion;
        do {
            String menuTexto = "--- Menú de Gestión ---\n" +
                    "1. Agregar nuevo alumno\n" +
                    "2. Eliminar alumno\n" +
                    "3. Agregar asignatura a alumno\n" +
                    "4. Eliminar asignatura de alumno\n" +
                    "5. Mostrar información de un alumno\n" +
                    "6. Listar todos los alumnos\n" +
                    "7. Salir";

            opcion = JOptionPane.showInputDialog(null, menuTexto, "Menú Principal", JOptionPane.PLAIN_MESSAGE);

            if (opcion == null) { // Si el usuario cierra la ventana
                opcion = "7";
            }

            switch (opcion) {
                case "1":
                    gestionAlumno.agregarNuevoAlumno();
                    break;
                case "2":
                    gestionAlumno.eliminarAlumno();
                    break;
                case "3":
                    gestionAsignatura.agregarAsignatura();
                    break;
                case "4":
                    gestionAsignatura.eliminarAsignatura();
                    break;
                case "5":
                    gestionAlumno.mostrarInformacion();
                    break;
                case "6":
                    gestionAlumno.listarAlumnos();
                    break;
                case "7":
                    JOptionPane.showMessageDialog(null, "Saliendo del programa...");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }

        } while (!opcion.equals("7"));
    }
}
