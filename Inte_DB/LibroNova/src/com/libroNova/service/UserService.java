package com.libroNova.service;

import com.libroNova.dao.LoanDaoImpl;
import com.libroNova.dao.UserDaoImpl;
import com.libroNova.exception.BusinessException;
import com.libroNova.interfas.PrestamoDao;
import com.libroNova.interfas.UserDao;
import com.libroNova.interfas.UserServiceInterface;
import com.libroNova.model.Prestamo;
import com.libroNova.model.User;

import java.util.List;
import java.util.Optional;

public class UserService implements UserServiceInterface {

    private final UserDao userDao;
    private final PrestamoDao prestamoDao;


    public UserService() {
        this.userDao = new UserDaoImpl();
        this.prestamoDao = new LoanDaoImpl();
    }

    @Override
    public User crearUsuario(User user) throws BusinessException {
        if (userDao.findByEmail(user.getEmail()).isPresent()) {
            throw new BusinessException("El email '" + user.getEmail() + "' ya está registrado.");
        }
        return userDao.create(user);
    }
    @Override
    public List<User> obtenerTodosLosUsuarios() {
        return userDao.getAll();
    }

    @Override
    public Optional<User> buscarUsuarioPorId(Integer id) {
        return userDao.searchById(id);
    }

    public User actualizarUsuario(User user) {
        return userDao.update(user);
    }

    @Override
    public boolean eliminarUsuario(Integer id) throws BusinessException {
        List<Prestamo> prestamosAsociados = prestamoDao.findByUsuarioId(id);
        if (!prestamosAsociados.isEmpty()) {
            throw new BusinessException("No se puede eliminar el usuario con ID " + id + " porque tiene " +
                    prestamosAsociados.size() + " préstamo(s) registrado(s).");
        }
        return userDao.delete(id);
    }
}