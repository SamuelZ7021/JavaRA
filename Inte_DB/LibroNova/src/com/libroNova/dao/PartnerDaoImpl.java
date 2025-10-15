package com.libroNova.dao;

import com.libroNova.exception.DataAccessException;
import com.libroNova.interfas.PartnerDAO;
import com.libroNova.model.Partner;
import com.libroNova.util.DatabaseConnector;
import com.libroNova.util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PartnerDaoImpl implements PartnerDAO {
    @Override
    public Partner create(Partner partner) {
        String sql = "INSERT INTO socios (nombre, apellido, cc, email, password_hash, telefono, direccion, estado) VALUES (?,?,?,?,?,?,?,?)";
        try(Connection conn = DatabaseConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, partner.getNombre());
            pstmt.setString(2, partner.getApellido());
            pstmt.setString(3, partner.getCc());
            pstmt.setString(4, partner.getEmail());
            pstmt.setString(5, partner.getPasswordHash());
            pstmt.setString(6, partner.getTelefono());
            pstmt.setString(7, partner.getDireccion());
            pstmt.setString(8, partner.getEstado());

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        partner.setId(generatedKeys.getInt(1));
                        return partner;
                    }
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al crear socio: " + partner, e);
            throw new DataAccessException("No se pudo crear el socio.", e);
        }
        return null;
    }

    @Override
    public Partner update(Partner partner) {
        String sql = "UPDATE socios SET nombre = ?, apellido = ?, cc = ?, email = ?, telefono = ?, direccion = ?, estado = ? WHERE id_socio = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, partner.getNombre());
            pstmt.setString(2, partner.getApellido());
            pstmt.setString(3, partner.getCc());
            pstmt.setString(4, partner.getEmail());
            pstmt.setString(5, partner.getTelefono());
            pstmt.setString(6, partner.getDireccion());
            pstmt.setString(7, partner.getEstado());
            pstmt.setInt(8, partner.getId());

            if (pstmt.executeUpdate() > 0) {
                return partner;
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al actualizar socio con ID: " + partner.getId(), e);
            throw new DataAccessException("No se pudo actualizar el socio.", e);
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM socios WHERE id_socio = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al eliminar socio con ID: " + id, e);
            throw new DataAccessException("No se pudo eliminar el socio.", e);
        }
    }

    @Override
    public Optional<Partner> searchById(Integer id) {
        String sql = "SELECT * FROM socios WHERE id_socio = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSocio(rs));
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al buscar socio por ID: " + id, e);
            throw new DataAccessException("Error al buscar el socio.", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Partner> findByCc(String cc) {
        String sql = "SELECT * FROM socios WHERE cc = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cc);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSocio(rs));
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al buscar socio por CC: " + cc, e);
            throw new DataAccessException("Error al buscar el socio.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Partner> getAll() {
        List<Partner> partners = new ArrayList<>();
        String sql = "SELECT * FROM socios";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                partners.add(mapResultSetToSocio(rs));
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al obtener todos los socios.", e);
            throw new DataAccessException("Error al obtener la lista de socios.", e);
        }
        return partners;
    }

    @Override
    public Optional<Partner> findByEmail(String email) {
        String sql = "SELECT * FROM socios WHERE email = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSocio(rs));
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al buscar socio por email: " + email, e);
            throw new DataAccessException("Error al buscar el socio por email.", e);
        }
        return Optional.empty();
    }


    private Partner mapResultSetToSocio(ResultSet rs) throws SQLException {
        return new Partner(
                rs.getInt("id_socio"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("cc"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("telefono"),
                rs.getString("direccion"),
                rs.getTimestamp("fecha_registro").toLocalDateTime(),
                rs.getString("estado")
        );
    }
}
