package service;

import model.User;
import java.util.List;

public interface UsuarioInterface {
    void registrarUsuario(User usuario);

    User iniciarSesion(String email, String password);

    List<User> obtenerTodosLosUsuarios();

    User obtenerUsuarioPorId(int id);

    void actualizarUsuario(User usuario);

    void eliminarUsuario(int id);
}
