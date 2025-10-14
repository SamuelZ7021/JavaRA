package com.bliNva.dao;

import com.bliNva.model.Socio;
import java.util.List;
import java.util.Optional;


public interface SocioDAO {
    Socio create(Socio socio);
    Socio update(Socio socio);
    boolean delete(Integer id);
    Optional<Socio> searchById(Integer id);
    List<Socio> getAll();
    Optional<Socio> findByCc(String cc);
}
