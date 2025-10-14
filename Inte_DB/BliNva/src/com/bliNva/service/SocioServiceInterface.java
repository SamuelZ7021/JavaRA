package com.bliNva.service;

import com.bliNva.exception.BusinessException;
import com.bliNva.model.Socio;

import java.util.List;
import java.util.Optional;

public interface SocioServiceInterface {
    Socio crearSocio(Socio socio) throws BusinessException;
    List<Socio> obtenerTodosLosSocios();
    Optional<Socio> buscarSocioPorId(Integer id);
    Socio actualizarSocio(Socio socio);
    boolean eliminarSocio(Integer id);
}
