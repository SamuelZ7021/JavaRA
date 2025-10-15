package com.libroNova.controller;
import javax.swing.*;

public class MenuController {

    private final BookController bookController;
    private final PartnerController partnerController;
    private final LoanController LoanController;
    private final UserController userController;
    private final ReporterController reporteController;

    public MenuController() {
        this.bookController = new BookController();
        this.partnerController = new PartnerController();
        this.LoanController = new LoanController();
        this.userController = new UserController();
        this.reporteController = new ReporterController();
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
                    bookController.gestionarLibros();
                    break;
                case "2":
                    partnerController.gestionarSocios();
                    break;
                case "3":
                    LoanController.gestionarPrestamos();
                    break;
                case "4":
                    userController.gestionarUsuarios();
                    break;
                case "5":
                    reporteController.gestionarReportes();
                    break;
                case "6":
                    JOptionPane.showMessageDialog(null, "¡Hasta pronto!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, elige una opción del menú.", "Error", JOptionPane.WARNING_MESSAGE);
                    break;
            }

        } while (opcion != null && !opcion.equals("6"));
    }
}