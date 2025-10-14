package com.bliNva.service;

import com.bliNva.Interface.SocioDAO;
import com.bliNva.Interface.SocioServiceInterface;
import com.bliNva.dao.SocioDAOImpl;
import com.bliNva.exception.BusinessException;
import com.bliNva.model.Socio;

import java.util.List;
import java.util.Optional;

public class SocioService implements SocioServiceInterface {

    private final SocioDAO socioDAO;

    public SocioService() {
        this.socioDAO = new SocioDAOImpl();
    }

    @Override
    public Socio crearSocio(Socio socio) throws BusinessException {
        if (socio.getCc() == null || socio.getCc().isEmpty()) {
            throw new BusinessException("El campo Cédula (CC) es obligatorio.");
        }

        Optional<Socio> socioExistente = socioDAO.findByCc(socio.getCc());
        if (socioExistente.isPresent()) {
            throw new BusinessException("Ya existe un socio con la Cédula: " + socio.getCc());
        }

        return socioDAO.create(socio);
    }

    @Override
    public List<Socio> obtenerTodosLosSocios() {
        return socioDAO.getAll();
    }

    @Override
    public Optional<Socio> buscarSocioPorId(Integer id) {
        return socioDAO.searchById(id);
    }

    @Override
    public Socio actualizarSocio(Socio socio) {
        return socioDAO.update(socio);
    }

    @Override
    public boolean eliminarSocio(Integer id) {
        return socioDAO.delete(id);
    }
}

