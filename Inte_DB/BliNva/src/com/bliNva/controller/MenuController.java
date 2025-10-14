package com.bliNva.controller;

import javax.swing.*;

public class MenuController {

    private final LibroController libroController;
    private final SocioController socioController;
    private final PrestamoController prestamoController;
    private final UsuarioController usuarioController;
    private final ReporteController reporteController;

    public MenuController() {
        this.libroController = new LibroController();
        this.socioController = new SocioController();
        this.prestamoController = new PrestamoController();
        this.usuarioController = new UsuarioController();
        this.reporteController = new ReporteController();
    }

    public void iniciar() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                    null,
                    "--- MENÚ PRINCIPAL DE LIBRONOVA ---\n" +
                            "1. Gestionar Libros\n" +
                            "2. Gestionar Socios\n" +
                            "3. Gestionar Préstamos\n" +
                            "4. Gestionar Usuarios\n" +
                            "5. Generar Reportes\n" +
                            "6. Salir\n\n" +
                            "Elige una opción:",
                    "LibroNova - Menú Principal",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) {
                break;
            }

            switch (opcion) {
                case "1":
                    libroController.gestionarLibros();
                    break;
                case "2":
                    socioController.gestionarSocios();
                    break;
                case "3":
                    prestamoController.gestionarPrestamos();
                    break;
                case "4":
                    usuarioController.gestionarUsuarios();
                    break;
                case "5":
                    reporteController.gestionarReportes();
                    break;
                case "6":
                    JOptionPane.showMessageDialog(null, "Gracias por usar LibroNova. ¡Hasta pronto!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, elige una opción del menú.", "Error", JOptionPane.WARNING_MESSAGE);
                    break;
            }

        } while (opcion != null && !opcion.equals("6"));
    }
}

