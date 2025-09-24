package app;

import model.Administrador;
import model.Cliente;
import model.User; // Import corregido
import service.UserService;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * Clase que gestiona la interfaz de usuario y la navegación entre menús.
 */
public class Menu {
    private final UserService userService = new UserService();

    // Menu pricipal, antes de insertar un usuario
    public void mostrarMenu() {
        while (true) {
            String opcion = JOptionPane.showInputDialog(
                    "Menú Principal\n" +
                    "1. Iniciar Sesión\n" +
                    "2. Registrarse como Cliente\n" +
                    "3. Salir"
            );

            if (opcion == null) {
                break;
            }

            switch (opcion) {
                case "1":
                    iniciarSesion(userService);
                    break;
                case "2":
                    registrarCliente(userService);
                    break;
                case "3":
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
                    break;
            }
        }
    }

    private void registrarCliente(UserService userService) {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String email = JOptionPane.showInputDialog("Email:");
        String password = JOptionPane.showInputDialog("Contraseña:");
        String direccion = JOptionPane.showInputDialog("Dirección:");
        String telefono = JOptionPane.showInputDialog("Teléfono:");

        if (nombre != null && !nombre.trim().isEmpty() && email != null && !email.trim().isEmpty()) {
            User nuevoCliente = new Cliente(nombre, email, password, true, direccion, telefono);
            userService.registrarUsuario(nuevoCliente);
            JOptionPane.showMessageDialog(null, "Cliente registrado exitosamente.");
        }
    }

    private void iniciarSesion(UserService userService) {
        String email = JOptionPane.showInputDialog("Email:");
        String password = JOptionPane.showInputDialog("Contraseña:");

        if (email != null && password != null) {
            User usuario = userService.iniciarSesion(email, password);
            if (usuario != null) {
                JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
                if (usuario instanceof Administrador) {
                    menuAdministrador(userService, (Administrador) usuario);
                } else if (usuario instanceof Cliente) {
                    menuCliente(userService, (Cliente) usuario);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Email o contraseña incorrectos, o usuario bloqueado.");
            }
        }
    }

    private void menuAdministrador(UserService userService, Administrador admin) {
        while (true) {
            String opcion = JOptionPane.showInputDialog(
                    "Menú de Administrador\n" +
                    "1. Ver todos los usuarios\n" +
                    "2. Gestionar bloqueo de usuario\n" +
                    "3. Ver mi perfil\n" +
                    "4. Cerrar Sesión"
            );

            if (opcion == null) {
                break;
            }

            switch (opcion) {
                case "1":
                    List<User> usuarios = userService.obtenerTodosLosUsuarios();
                    StringBuilder listaUsuarios = new StringBuilder("Lista de Usuarios:\n");
                    for (User u : usuarios) {
                        // Corregido: Llamar a mostrarPerfil() para obtener la información completa.
                        listaUsuarios.append(u.mostrarPerfil()).append("\n--------------------\n");
                    }
                    JOptionPane.showMessageDialog(null, listaUsuarios.toString());
                    break;
                case "2":
                    String idUsuarioStr = JOptionPane.showInputDialog("Ingrese el ID del usuario a gestionar:");
                    try {
                        int idUsuario = Integer.parseInt(idUsuarioStr);
                        User usuarioAGestionar = userService.obtenerUsuarioPorId(idUsuario);
                        if (usuarioAGestionar != null) {
                            String accion = JOptionPane.showInputDialog("¿Desea 'bloquear' o 'desbloquear' al usuario?");
                            boolean bloquear = "bloquear".equalsIgnoreCase(accion);
                            
                            // Corregido: Llamar al método del servicio, que maneja la transacción y la bitácora.
                            userService.gestionarBloqueoUsuario(admin, usuarioAGestionar, bloquear);

                        } else {
                            JOptionPane.showMessageDialog(null, "Usuario no encontrado.");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "ID inválido. Por favor, ingrese un número.");
                    }
                    break;
                case "3":
                    JOptionPane.showMessageDialog(null, admin.mostrarPerfil());
                    break;
                case "4":
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
                    break;
            }
        }
    }

    private void menuCliente(UserService userService, Cliente cliente) {
        while (true) {
            String opcion = JOptionPane.showInputDialog(
                    "Menú de Cliente\n" +
                    "1. Actualizar datos de contacto\n" +
                    "2. Ver mi perfil\n" +
                    "3. Cerrar Sesión"
            );

            if (opcion == null) {
                break;
            }

            switch (opcion) {
                case "1":
                    String nuevaDireccion = JOptionPane.showInputDialog("Nueva dirección:", cliente.getDireccion());
                    String nuevoTelefono = JOptionPane.showInputDialog("Nuevo teléfono:", cliente.getTelefono());
                    cliente.actualizarContacto(nuevoTelefono, nuevaDireccion);
                    userService.actualizarUsuario(cliente);
                    JOptionPane.showMessageDialog(null, "Contacto actualizado.");
                    break;
                case "2":
                    JOptionPane.showMessageDialog(null, cliente.mostrarPerfil());
                    break;
                case "3":
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
                    break;
            }
        }
    }
}
