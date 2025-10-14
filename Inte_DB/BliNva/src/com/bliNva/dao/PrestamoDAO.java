package com.bliNva.dao;

import com.bliNva.model.Prestamo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PrestamoDAO {
    Prestamo createWithConnection(Prestamo prestamo, Connection conn) throws SQLException;
    Prestamo updateWithConnection(Prestamo prestamo, Connection conn) throws SQLException;

    Optional<Prestamo> findById(Integer id);
    List<Prestamo> findAll();
    List<Prestamo> findActiveLoansBySocioId(Integer socioId);
    List<Prestamo> findOverdueLoans();
    List<Prestamo> findByUsuarioId(Integer usuarioId);
}

