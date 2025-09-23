package app;

import model.Administrador;
import model.Cliente;
import model.User;
import service.UserService;

import javax.swing.JOptionPane;
import java.util.List;


public class Menu {
    // Instancia del servicio de usuarios para gestionar las operaciones.
    private final UserService userService = new UserService();

     //Muestra el menú principal de la aplicación y gestiona las opciones del usuario.
    public void mostrarMenu() {
        // --- Datos de prueba para iniciar la aplicación con usuarios predefinidos ---
        User admin = new Administrador("Admin", "admin@test.com", "admin123", true);
        User cliente = new Cliente("Cliente", "cliente@test.com", "cliente123", false, "Calle Falsa 123", "555-1234");
        userService.registrarUsuario(admin);
        userService.registrarUsuario(cliente);

        // Bucle infinito para mantener el menú activo.
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
                    System.exit(0); // Termina la aplicación.
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
                    break;
            }
        }
    }

    // Permite al usuario registrarse como un nuevo cliente.
    private void registrarCliente(UserService userService) {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String email = JOptionPane.showInputDialog("Email:");
        String password = JOptionPane.showInputDialog("Contraseña:");
        String direccion = JOptionPane.showInputDialog("Dirección:");
        String telefono = JOptionPane.showInputDialog("Teléfono:");

        // Valida que todos los campos estén llenos antes de crear el cliente.
        if (nombre != null && email != null && password != null && direccion != null && telefono != null) {
            User nuevoCliente = new Cliente(nombre, email, password, true, direccion, telefono);
            userService.registrarUsuario(nuevoCliente);
        }
    }

    // Gestiona el proceso de inicio de sesión del usuario.
    private void iniciarSesion(UserService userService) {
        String email = JOptionPane.showInputDialog("Email:");
        String password = JOptionPane.showInputDialog("Contraseña:");

        if (email != null && password != null) {
            User usuario = userService.iniciarSesion(email, password);
            if (usuario != null) {
                JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso.");
                // Redirige al menú correspondiente según el tipo de usuario.
                if (usuario instanceof Administrador) {
                    menuAdministrador(userService, (Administrador) usuario);
                } else if (usuario instanceof Cliente) {
                    menuCliente(userService, (Cliente) usuario);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Email o contraseña incorrectos.");
            }
        }
    }

    // Muestra el menú específico para usuarios Administradores.
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
                            admin.gestionarBloqueoUsuario(usuarioAGestionar, bloquear);
                            JOptionPane.showMessageDialog(null, "Estado del usuario actualizado.");
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
                    return; // Sale del menú del administrador.
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
                    break;
            }
        }
    }

    // Muestra el menú específico para usuarios Clientes.
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
                    userService.actualizarUsuario(cliente); // Notifica al servicio sobre la actualización.
                    JOptionPane.showMessageDialog(null, "Contacto actualizado.");
                    break;
                case "2":
                    JOptionPane.showMessageDialog(null, cliente.mostrarPerfil());
                    break;
                case "3":
                    return; // Sale del menú del cliente.
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
                    break;
            }
        }
    }
}
