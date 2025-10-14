package com.bliNva.dao;

import com.bliNva.Interface.SocioDAO;
import com.bliNva.exception.DataAccessException;
import com.bliNva.model.Socio;
import com.bliNva.util.DatabaseConnector;
import com.bliNva.util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SocioDAOImpl implements SocioDAO {

    @Override
    public Socio create(Socio socio) {
        String sql = "INSERT INTO socios (nombre, apellido, cc, email, telefono, direccion, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, socio.getNombre());
            pstmt.setString(2, socio.getApellido());
            pstmt.setString(3, socio.getCc());
            pstmt.setString(4, socio.getEmail());
            pstmt.setString(5, socio.getTelefono());
            pstmt.setString(6, socio.getDireccion());
            pstmt.setString(7, socio.getEstado());

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        socio.setId(generatedKeys.getInt(1));
                        return socio;
                    }
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al crear socio: " + socio, e);
            throw new DataAccessException("No se pudo crear el socio.", e);
        }
        return null;
    }

    @Override
    public Socio update(Socio socio) {
        String sql = "UPDATE socios SET nombre = ?, apellido = ?, cc = ?, email = ?, telefono = ?, direccion = ?, estado = ? WHERE id_socio = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, socio.getNombre());
            pstmt.setString(2, socio.getApellido());
            pstmt.setString(3, socio.getCc());
            pstmt.setString(4, socio.getEmail());
            pstmt.setString(5, socio.getTelefono());
            pstmt.setString(6, socio.getDireccion());
            pstmt.setString(7, socio.getEstado());
            pstmt.setInt(8, socio.getId());

            if (pstmt.executeUpdate() > 0) {
                return socio;
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al actualizar socio con ID: " + socio.getId(), e);
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
    public Optional<Socio> searchById(Integer id) {
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
    public Optional<Socio> findByCc(String cc) {
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
    public List<Socio> getAll() {
        List<Socio> socios = new ArrayList<>();
        String sql = "SELECT * FROM socios";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                socios.add(mapResultSetToSocio(rs));
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al obtener todos los socios.", e);
            throw new DataAccessException("Error al obtener la lista de socios.", e);
        }
        return socios;
    }


    private Socio mapResultSetToSocio(ResultSet rs) throws SQLException {
        return new Socio(
                rs.getInt("id_socio"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("cc"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("direccion"),
                rs.getTimestamp("fecha_registro").toLocalDateTime(),
                rs.getString("estado")
        );
    }
}

