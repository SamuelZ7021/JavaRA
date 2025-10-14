package com.bliNva.Interface;

import com.bliNva.exception.BusinessException;
import com.bliNva.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioServiceInterface {
    Usuario crearUsuario(Usuario usuario) throws BusinessException;
    List<Usuario> obtenerTodosLosUsuarios();
    Optional<Usuario> buscarUsuarioPorId(Integer id);
    Usuario actualizarUsuario(Usuario usuario);
    boolean eliminarUsuario(Integer id) throws BusinessException;
}

