package com.bliNva.Interface;

import com.bliNva.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioDAO {
    Usuario create(Usuario usuario);
    Usuario update(Usuario usuario);
    boolean delete(Integer id);
    Optional<Usuario> searchById(Integer id);
    List<Usuario> getAll();
    Optional<Usuario> findByEmail(String email);
}

