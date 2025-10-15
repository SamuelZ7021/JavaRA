package com.libroNova.service;


import com.libroNova.dao.PartnerDaoImpl;
import com.libroNova.exception.BusinessException;
import com.libroNova.interfas.PartnerDAO;
import com.libroNova.interfas.PartnerServiceInterface;
import com.libroNova.model.Partner;

import java.util.List;
import java.util.Optional;

public class PartnerService implements PartnerServiceInterface {

    private final PartnerDAO partnerDAO;

    public PartnerService() {
        this.partnerDAO = new PartnerDaoImpl();
    }

    @Override
    public Partner crearSocio(Partner partner) throws BusinessException {
        if (partner.getCc() == null || partner.getCc().isEmpty()) {
            throw new BusinessException("El campo Cédula (CC) es obligatorio.");
        }

        Optional<Partner> socioExistente = partnerDAO.findByCc(partner.getCc());
        if (socioExistente.isPresent()) {
            throw new BusinessException("Ya existe un socio con la Cédula: " + partner.getCc());
        }

        return partnerDAO.create(partner);
    }

    @Override
    public List<Partner> obtenerTodosLosSocios() {
        return partnerDAO.getAll();
    }

    @Override
    public Optional<Partner> buscarSocioPorId(Integer id) {
        return partnerDAO.searchById(id);
    }

    @Override
    public Partner actualizarSocio(Partner partner) {
        return partnerDAO.update(partner);
    }

    @Override
    public boolean eliminarSocio(Integer id) {
        return partnerDAO.delete(id);
    }
}