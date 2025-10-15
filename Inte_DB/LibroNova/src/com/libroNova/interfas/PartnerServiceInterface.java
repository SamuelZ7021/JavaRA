package com.libroNova.interfas;


import com.libroNova.exception.BusinessException;
import com.libroNova.model.Partner;

import java.util.List;
import java.util.Optional;

public interface PartnerServiceInterface {
    Partner crearSocio(Partner partner) throws BusinessException;
    List<Partner> obtenerTodosLosSocios();
    Optional<Partner> buscarSocioPorId(Integer id);
    Partner actualizarSocio(Partner partner);
    boolean eliminarSocio(Integer id);
}