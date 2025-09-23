package service;

import model.User;
import java.util.List;

/**
 * Interfaz que define el contrato para los servicios de gestión de usuarios.
 * Abstrae las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) y de autenticación.
 */
public interface UsuarioInterface {

    /**
     * Registra un nuevo usuario en el sistema.
     * @param usuario El usuario a registrar.
     */
    void registrarUsuario(User usuario);

    /**
     * Autentica a un usuario y, si es exitoso, devuelve el objeto Usuario.
     * @param email El email del usuario.
     * @param password La contraseña del usuario.
     * @return El objeto User si la sesión se inicia correctamente, o null si falla.
     */
    User iniciarSesion(String email, String password);

    /**
     * Devuelve una lista con todos los usuarios registrados.
     * @return Una lista de objetos User.
     */
    List<User> obtenerTodosLosUsuarios();

    /**
     * Busca y devuelve un usuario por su ID.
     * @param id El ID del usuario a buscar.
     * @return El objeto User si se encuentra, o null si no.
     */
    User obtenerUsuarioPorId(int id);

    /**
     * Actualiza la información de un usuario existente.
     * @param usuario El usuario con los datos actualizados.
     */
    void actualizarUsuario(User usuario);

    /**
     * Elimina un usuario del sistema por su ID.
     * @param id El ID del usuario a eliminar.
     */
    void eliminarUsuario(int id);
}
