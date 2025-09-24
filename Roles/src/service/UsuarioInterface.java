package service;

import model.User;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz que define el contrato para los servicios de gestión de usuarios.
 * Abstrae las operaciones CRUD y de autenticación, permitiendo que los errores de BD se propaguen.
 */
public interface UsuarioInterface {

    // Registra un nuevo usuario en el sistema.
    void registrarUsuario(User usuario) throws SQLException;

    // Autentica a un usuario.
    User iniciarSesion(String email, String password) throws SQLException;

    // Devuelve una lista con todos los usuarios registrados.
    List<User> obtenerTodosLosUsuarios() throws SQLException;

    // Busca y devuelve un usuario por su ID.
    User obtenerUsuarioPorId(int id) throws SQLException;

    // Actualiza la información de un usuario existente.
    void actualizarUsuario(User usuario) throws SQLException;

    // Elimina un usuario del sistema por su ID.

    void eliminarUsuario(int id) throws SQLException;
}
