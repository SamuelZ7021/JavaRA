package com.libroNova.service;

import com.libroNova.dao.PartnerDaoImpl;
import com.libroNova.dao.UserDaoImpl;
import com.libroNova.exception.BusinessException;
import com.libroNova.interfas.PartnerDAO;
import com.libroNova.interfas.UserDao;
import com.libroNova.model.Partner;
import com.libroNova.model.User;

import java.util.Optional;

public class AuthService {

    private final UserDao userDao = new UserDaoImpl();
    private final PartnerDAO partnerDAO = new PartnerDaoImpl();

    public User loginUsuario(String email, String password) throws BusinessException {
        Optional<User> usuarioOpt = userDao.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw new BusinessException("Usuario no encontrado con el email: " + email);
        }
        User user = usuarioOpt.get();
        if (!user.getPasswordHash().equals(password)) {
            throw new BusinessException("Contraseña incorrecta.");
        }
        return user;
    }

    public Partner loginSocio(String email, String password) throws BusinessException {
        Optional<Partner> socioOpt = partnerDAO.findByEmail(email);
        if (socioOpt.isEmpty()) {
            throw new BusinessException("Socio no encontrado con el email: " + email);
        }
        Partner partner = socioOpt.get();
        if (!partner.getPasswordHash().equals(password)) {
            throw new BusinessException("Contraseña incorrecta.");
        }
        if (!partner.getEstado().equalsIgnoreCase("activo")) {
            throw new BusinessException("La cuenta de este socio no está activa.");
        }
        return partner;
    }
}
