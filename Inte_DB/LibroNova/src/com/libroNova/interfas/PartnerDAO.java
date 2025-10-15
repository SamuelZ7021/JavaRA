package com.libroNova.interfas;

import com.libroNova.model.Partner;

import java.util.List;
import java.util.Optional;


public interface PartnerDAO {
    Partner create(Partner partner);
    Partner update(Partner partner);
    boolean delete(Integer id);
    Optional<Partner> searchById(Integer id);
    List<Partner> getAll();
    Optional<Partner> findByCc(String cc);
    Optional<Partner> findByEmail(String email);
}