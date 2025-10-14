package com.bliNva.dao.impl;

import com.bliNva.dao.LibroDAO;
import com.bliNva.exception.DataAccessException;
import com.bliNva.model.Libro;
import com.bliNva.util.DatabaseConnector;
import com.bliNva.util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibroDAOImpl implements LibroDAO<Libro, Integer> {

    @Override
    public Libro create(Libro libro) {
        String sql = "INSERT INTO libros (isbn, titulo, autor, editorial, anio_publicacion, genero, stock_total, stock_disponible) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, libro.getIsbn());
            pstmt.setString(2, libro.getTitulo());
            pstmt.setString(3, libro.getAutor());
            pstmt.setString(4, libro.getEditorial());

            if (libro.getAnioPublicacion() != null) {
                pstmt.setInt(5, libro.getAnioPublicacion());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }

            pstmt.setString(6, libro.getGenero());
            pstmt.setInt(7, libro.getStockTotal());
            pstmt.setInt(8, libro.getStockDisponible());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        libro.setId(generatedKeys.getInt(1));
                        return libro;
                    }
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al crear el libro: " + libro, e);
            throw new DataAccessException("Error al crear el libro.", e);
        }
        return null;
    }

    @Override
    public Libro update(Libro libro) {
        try (Connection conn = DatabaseConnector.getConnection()){
            return updateWithConnection(libro, conn);
        } catch(SQLException e) {
            LoggerUtil.logError("Error de SQL al actualizar el libro: " + libro.getId(), e);
            throw new DataAccessException("Error al actualizar el libro.", e);
        }
    }

    /**
     * Método para actualizar un libro usando una conexión existente (para transacciones).
     */
    public Libro updateWithConnection(Libro libro, Connection conn) throws SQLException {
        String sql = "UPDATE libros SET isbn = ?, titulo = ?, autor = ?, editorial = ?, anio_publicacion = ?, genero = ?, stock_total = ?, stock_disponible = ? WHERE id_libro = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libro.getIsbn());
            pstmt.setString(2, libro.getTitulo());
            pstmt.setString(3, libro.getAutor());
            pstmt.setString(4, libro.getEditorial());

            if (libro.getAnioPublicacion() != null) {
                pstmt.setInt(5, libro.getAnioPublicacion());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            pstmt.setString(6, libro.getGenero());
            pstmt.setInt(7, libro.getStockTotal());
            pstmt.setInt(8, libro.getStockDisponible());
            pstmt.setInt(9, libro.getId());

            if (pstmt.executeUpdate() > 0) {
                return libro;
            }
        }
        return null;
    }


    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM libros WHERE id_libro = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al eliminar el libro con ID: " + id, e);
            throw new DataAccessException("Error al eliminar el libro.", e);
        }
    }

    @Override
    public Optional<Libro> searchById(Integer id) {
        String sql = "SELECT * FROM libros WHERE id_libro = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToLibro(rs));
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al buscar el libro por ID: " + id, e);
            throw new DataAccessException("Error al buscar libro por ID.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Libro> getAll() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                libros.add(mapResultSetToLibro(rs));
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al obtener todos los libros.", e);
            throw new DataAccessException("Error al listar todos los libros.", e);
        }
        return libros;
    }

    private Libro mapResultSetToLibro(ResultSet rs) throws SQLException {
        return new Libro(
                rs.getInt("id_libro"),
                rs.getString("isbn"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("editorial"),
                rs.getObject("anio_publicacion", Integer.class),
                rs.getString("genero"),
                rs.getInt("stock_total"),
                rs.getInt("stock_disponible")
        );
    }
}

