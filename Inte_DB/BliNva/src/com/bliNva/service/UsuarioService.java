package com.bliNva.service;

import com.bliNva.dao.PrestamoDAO;
import com.bliNva.dao.UsuarioDAO;
import com.bliNva.dao.impl.PrestamoDAOImpl;
import com.bliNva.dao.impl.UsuarioDAOImpl;
import com.bliNva.exception.BusinessException;
import com.bliNva.model.Prestamo;
import com.bliNva.model.Usuario;

import java.util.List;
import java.util.Optional;

public class UsuarioService implements UsuarioServiceInterface {

    private final UsuarioDAO usuarioDAO;
    private final PrestamoDAO prestamoDAO;


    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAOImpl();
        this.prestamoDAO = new PrestamoDAOImpl();
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) throws BusinessException {
        if (usuarioDAO.findByEmail(usuario.getEmail()).isPresent()) {
            throw new BusinessException("El email '" + usuario.getEmail() + "' ya está registrado.");
        }
        return usuarioDAO.create(usuario);
    }
    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioDAO.getAll();
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorId(Integer id) {
        return usuarioDAO.searchById(id);
    }

    public Usuario actualizarUsuario(Usuario usuario) {
        return usuarioDAO.update(usuario);
    }

    @Override
    public boolean eliminarUsuario(Integer id) throws BusinessException {
        List<Prestamo> prestamosAsociados = prestamoDAO.findByUsuarioId(id);
        if (!prestamosAsociados.isEmpty()) {
            throw new BusinessException("No se puede eliminar el usuario con ID " + id + " porque tiene " +
                    prestamosAsociados.size() + " préstamo(s) registrado(s).");
        }
        return usuarioDAO.delete(id);
    }
}
