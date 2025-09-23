package service;

import model.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación del servicio de gestión de usuarios.
 * Utiliza un mapa en memoria para almacenar y gestionar los datos de los usuarios.
 */
public class UserService implements UsuarioInterface {

    // Mapa para almacenar usuarios. La clave es el email (String) y el valor es el objeto User.
    private final Map<String, User> usuarios = new HashMap<>();

    // Registra un nuevo usuario, validando que no sea nulo y que el email no exista previamente.
    @Override
    public void registrarUsuario(User usuario) {
        if(usuario == null || usuario.getEmail() == null){
            JOptionPane.showMessageDialog(null, "Error: El usuario o el email no pueden ser nulos.");
            return;
        }
        if(usuarios.containsKey(usuario.getEmail())){
            JOptionPane.showMessageDialog(null, "Error: Ya existe un usuario con el email " + usuario.getEmail() + ".");
            return;
        }
        usuarios.put(usuario.getEmail(), usuario);
        // No se muestra mensaje aquí para no interrumpir el flujo de inicialización de datos.
    }

    // Inicia sesión validando el email y la contraseña.
    @Override
    public User iniciarSesion(String email, String password) {
        User usuario = usuarios.get(email);
        if( usuario != null && usuario.login(password)){
            return usuario; // Devuelve el usuario si el login es exitoso.
        }
        return null; // Falla el login.
    }

    // Devuelve una copia de la lista de todos los usuarios.
    @Override
    public List<User> obtenerTodosLosUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    // Busca un usuario por su ID numérico.
    @Override
    public User obtenerUsuarioPorId(int id) {
        for (User user : usuarios.values()){
            if(user.getId() == id){
                return user;
            }
        }
        return null; // No se encontró el usuario.
    }

    // Actualiza un usuario si existe en el mapa.
    @Override
    public void actualizarUsuario(User usuario) {
        if (usuario != null && usuarios.containsKey(usuario.getEmail())){
            usuarios.put(usuario.getEmail(), usuario);
            JOptionPane.showMessageDialog(null, "Usuario actualizado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Error: No se pudo actualizar porque el usuario no se encontró.");
        }
    }

    // Elimina un usuario buscando su ID.
    @Override
    public void eliminarUsuario(int id) {
        String emailParaEliminar = null;
        // Itera para encontrar el email asociado al ID.
        for (Map.Entry<String, User> entry : usuarios.entrySet()){
            if (entry.getValue().getId() == id){
                emailParaEliminar = entry.getKey();
                break;
            }
        }
        
        if (emailParaEliminar != null){
            usuarios.remove(emailParaEliminar);
            JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Error: No se pudo eliminar porque el usuario no se encontró.");
        }
    }
}
