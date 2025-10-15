package com.libroNova.interfas;

import com.libroNova.exception.BusinessException;
import com.libroNova.model.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    User crearUsuario(User user) throws BusinessException;
    List<User> obtenerTodosLosUsuarios();
    Optional<User> buscarUsuarioPorId(Integer id);
    User actualizarUsuario(User user);
    boolean eliminarUsuario(Integer id) throws BusinessException;
}
