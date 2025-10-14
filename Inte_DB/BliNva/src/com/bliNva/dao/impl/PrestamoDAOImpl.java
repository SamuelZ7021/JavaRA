package com.bliNva.dao.impl;

import com.bliNva.dao.PrestamoDAO;
import com.bliNva.exception.DataAccessException;
import com.bliNva.model.Prestamo;
import com.bliNva.util.DatabaseConnector;
import com.bliNva.util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrestamoDAOImpl implements PrestamoDAO {

    @Override
    public Prestamo createWithConnection(Prestamo prestamo, Connection conn) throws SQLException {
        String sql = "INSERT INTO prestamos (id_libro, id_socio, id_usuario_prestador, fecha_devolucion_estimada, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, prestamo.getIdLibro());
            pstmt.setInt(2, prestamo.getIdSocio());
            pstmt.setInt(3, prestamo.getIdUsuarioPrestador());
            pstmt.setTimestamp(4, Timestamp.valueOf(prestamo.getFechaDevolucionEstimada()));
            pstmt.setString(5, prestamo.getEstado());

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        prestamo.setId(rs.getInt(1));
                        updatePrestamoConDatosGenerados(prestamo, conn);
                        return prestamo;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Prestamo updateWithConnection(Prestamo prestamo, Connection conn) throws SQLException {
        String sql = "UPDATE prestamos SET fecha_devolucion_real = ?, estado = ? WHERE id_prestamo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (prestamo.getFechaDevolucionReal() != null) {
                pstmt.setTimestamp(1, Timestamp.valueOf(prestamo.getFechaDevolucionReal()));
            } else {
                pstmt.setNull(1, Types.TIMESTAMP);
            }
            pstmt.setString(2, prestamo.getEstado());
            pstmt.setInt(3, prestamo.getId());

            if (pstmt.executeUpdate() > 0) {
                return prestamo;
            }
        }
        return null;
    }

    @Override
    public Optional<Prestamo> findById(Integer id) {
        String sql = "SELECT * FROM prestamos WHERE id_prestamo = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPrestamo(rs));
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al obtener el prestamo por ID: " + id, e);
            throw new DataAccessException("Error al buscar préstamo por ID.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Prestamo> findAll() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos ORDER BY fecha_prestamo DESC";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                prestamos.add(mapResultSetToPrestamo(rs));
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al obtener todos los prestamos: ", e);
            throw new DataAccessException("Error al listar todos los préstamos.", e);
        }
        return prestamos;
    }

    @Override
    public List<Prestamo> findOverdueLoans() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE estado = 'activo' AND fecha_devolucion_estimada < NOW()";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                prestamos.add(mapResultSetToPrestamo(rs));
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al buscar préstamos vencidos.", e);
            throw new DataAccessException("No se pudo obtener la lista de préstamos vencidos.", e);
        }
        return prestamos;
    }

    @Override
    public List<Prestamo> findByUsuarioId(Integer usuarioId) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE id_usuario_prestador = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    prestamos.add(mapResultSetToPrestamo(rs));
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al buscar préstamos por ID de usuario: " + usuarioId, e);
            throw new DataAccessException("No se pudo obtener la lista de préstamos por usuario.", e);
        }
        return prestamos;
    }

    @Override
    public List<Prestamo> findActiveLoansBySocioId(Integer socioId) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE id_socio = ? AND estado = 'activo'";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, socioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    prestamos.add(mapResultSetToPrestamo(rs));
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al buscar préstamos activos por ID de socio: " + socioId, e);
            throw new DataAccessException("Error al buscar préstamos activos por socio.", e);
        }
        return prestamos;
    }

    private Prestamo mapResultSetToPrestamo(ResultSet rs) throws SQLException {
        Timestamp devolucionRealTS = rs.getTimestamp("fecha_devolucion_real");
        return new Prestamo(
                rs.getInt("id_prestamo"),
                rs.getInt("id_libro"),
                rs.getInt("id_socio"),
                rs.getInt("id_usuario_prestador"),
                rs.getTimestamp("fecha_prestamo").toLocalDateTime(),
                rs.getTimestamp("fecha_devolucion_estimada").toLocalDateTime(),
                devolucionRealTS != null ? devolucionRealTS.toLocalDateTime() : null,
                rs.getString("estado")
        );
    }

    /**
     * Método auxiliar para obtener la fecha de préstamo generada por la BD y actualizar el objeto.
     */
    private void updatePrestamoConDatosGenerados(Prestamo prestamo, Connection conn) throws SQLException {
        String sql = "SELECT fecha_prestamo FROM prestamos WHERE id_prestamo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, prestamo.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    prestamo.setFechaPrestamo(rs.getTimestamp("fecha_prestamo").toLocalDateTime());
                }
            }
        }
    }
}

