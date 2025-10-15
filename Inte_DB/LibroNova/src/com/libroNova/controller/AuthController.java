package com.libroNova.controller;
import com.libroNova.exception.BusinessException;
import com.libroNova.model.Partner;
import com.libroNova.model.User;
import com.libroNova.service.AuthService;
import com.libroNova.util.LoggerUtil;

import javax.swing.*;

public class AuthController {
    private final AuthService authService = new AuthService();
    private final MenuController menuController = new MenuController();
    private final PartnerController PartnerController = new PartnerController();
    private final UserController userController = new UserController();

    public void iniciar() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                    null,
                    "--- BIENVENIDO A LIBRONOVA ---\n" +
                            "1. Iniciar Sesión\n" +
                            "2. Registrarse\n" +
                            "3. Salir\n\n" +
                            "Elige una opción:",
                    "LibroNova",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) opcion = "3"; // Salir si cierra el diálogo

            switch (opcion) {
                case "1":
                    iniciarSesion();
                    break;
                case "2":
                    registrar();
                    break;
                case "3":
                    JOptionPane.showMessageDialog(null, "¡Gracias por usar LibroNova!.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
                    break;
            }
        } while (!opcion.equals("3"));
    }

    private void iniciarSesion() {
        try {
            String[] roles = {"Usuario (Admin)", "Socio"};
            String rolSeleccionado = (String) JOptionPane.showInputDialog(null, "Iniciar sesión como:",
                    "Seleccionar Rol", JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);

            if (rolSeleccionado == null) return;

            String email = JOptionPane.showInputDialog("Email:");
            String password = JOptionPane.showInputDialog("Contraseña:");

            if (rolSeleccionado.equals("Usuario (Admin)")) {
                User user = authService.loginUsuario(email, password);
                JOptionPane.showMessageDialog(null, "Bienvenido, " + user.getNombre() + "!");
                menuController.iniciar(); // Inicia el menú de administrador
            } else {
                Partner partner = authService.loginSocio(email, password);
                JOptionPane.showMessageDialog(null, "Bienvenido, " + partner.getNombre() + "!");
                // Aquí iría el menú del socio
                mostrarMenuSocio(partner);
            }
        } catch (BusinessException e) {
            LoggerUtil.logWarning("Intento de login fallido: " + e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            LoggerUtil.logError("Error inesperado en el login.", e);
            JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrar() {
        String[] roles = {"Registrarme como Socio", "Registrar un nuevo Usuario (Admin)"};
        String rolSeleccionado = (String) JOptionPane.showInputDialog(null, "Selecciona una opción de registro:",
                "Registro", JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);

        if (rolSeleccionado == null) return;

        if (rolSeleccionado.equals("Registrarme como Socio")) {
            PartnerController.crearSocio();
        } else {
            userController.crearUsuario();
        }
    }


    private void mostrarMenuSocio(Partner partner) {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                    null,
                    "--- MENÚ DE SOCIO ---\n" +
                            "1. Ver Catálogo de Libros\n" +
                            "2. Solicitar Préstamo\n" +
                            "3. Salir (Cerrar Sesión)\n\n" +
                            "Elige una opción:",
                    "Portal de Socio: " + partner.getNombre(),
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) opcion = "3";

            switch (opcion) {
                case "1":
                    new BookController().listarLibros();
                    break;
                case "2":
                    new LoanController().registrarNuevoPrestamo(partner.getId());
                    break;
                case "3":
                    JOptionPane.showMessageDialog(null, "Sesión cerrada.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
                    break;
            }
        } while (!opcion.equals("3"));
    }
}
