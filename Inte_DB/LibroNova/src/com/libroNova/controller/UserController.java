package com.libroNova.controller;


import com.libroNova.exception.BusinessException;
import com.libroNova.interfas.UserServiceInterface;
import com.libroNova.model.User;
import com.libroNova.service.UserService;
import com.libroNova.util.LoggerUtil;

import javax.swing.*;
import java.util.List;

public class UserController {
    private final UserServiceInterface usuarioService;

    public UserController() {
        this.usuarioService = new UserService();
    }

    public void gestionarUsuarios() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                    null,
                    "--- GESTIÓN DE USUARIOS ---\n" +
                            "1. Añadir nuevo usuario\n" +
                            "2. Listar todos los usuarios\n" +
                            "3. Eliminar un usuario\n" +
                            "4. Volver al Menú Principal\n\n" +
                            "Elige una opción:",
                    "Menú de Usuarios",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == null) break;

            switch (opcion) {
                case "1":
                    crearUsuario();
                    break;
                case "2":
                    listarUsuarios();
                    break;
                case "3":
                    eliminarUsuario();
                    break;
            }
        } while (opcion != null && !opcion.equals("4"));
    }

    public void crearUsuario() {
        try {
            String nombre = JOptionPane.showInputDialog("Nombre del usuario:");
            String apellido = JOptionPane.showInputDialog("Apellido del usuario:");
            String email = JOptionPane.showInputDialog("Email del usuario:");
            String password = JOptionPane.showInputDialog("Contraseña temporal:");
            int idRol = Integer.parseInt(JOptionPane.showInputDialog("ID del Rol (1=Admin, 2=Bibliotecario):"));

            User nuevoUser = new User();
            nuevoUser.setNombre(nombre);
            nuevoUser.setApellido(apellido);
            nuevoUser.setEmail(email);
            nuevoUser.setPasswordHash(password); // Temporalmente en texto plano
            nuevoUser.setIdRol(idRol);

            User creado = usuarioService.crearUsuario(nuevoUser);
            JOptionPane.showMessageDialog(null, "Usuario creado con éxito. ID: " + creado.getId());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID del Rol debe ser un número.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarUsuarios() {
        List<User> users = usuarioService.obtenerTodosLosUsuarios();
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios registrados.");
        } else {
            StringBuilder sb = new StringBuilder("--- LISTADO DE USUARIOS ---\n\n");
            for (User u : users) {
                sb.append("ID: ").append(u.getId())
                        .append(" | Nombre: ").append(u.getNombre()).append(" ").append(u.getApellido())
                        .append(" | Email: ").append(u.getEmail())
                        .append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Listado de Usuarios", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void eliminarUsuario() {
        try {
            String idStr = JOptionPane.showInputDialog("Introduce el ID del usuario a eliminar:");
            if (idStr == null) return; // El usuario canceló

            int id = Integer.parseInt(idStr);
            int confirmacion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Estás seguro de que deseas eliminar al usuario con ID " + id + "?\n" +
                            "Esta acción no se puede deshacer.",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (usuarioService.eliminarUsuario(id)) {
                    JOptionPane.showMessageDialog(null, "Usuario eliminado con éxito.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró un usuario con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Debes introducir un ID numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (BusinessException e) {
            LoggerUtil.logWarning("Intento de eliminación fallido: " + e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Acción no permitida", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            LoggerUtil.logError("Error inesperado al eliminar usuario.", e);
            JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }
}
