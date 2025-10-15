package com.libroNova.dao;
import com.libroNova.interfas.BookDao;
import com.libroNova.exception.DataAccessException;
import com.libroNova.model.Book;
import com.libroNova.util.DatabaseConnector;
import com.libroNova.util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl implements BookDao<Book, Integer> {

    @Override
    public Book create(Book book) {
        String sql = "INSERT INTO libros (isbn, titulo, autor, editorial, anio_publicacion, genero, stock_total, stock_disponible) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitulo());
            pstmt.setString(3, book.getAutor());
            pstmt.setString(4, book.getEditorial());

            if (book.getAnioPublicacion() != null) {
                pstmt.setInt(5, book.getAnioPublicacion());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }

            pstmt.setString(6, book.getGenero());
            pstmt.setInt(7, book.getStockTotal());
            pstmt.setInt(8, book.getStockDisponible());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setId(generatedKeys.getInt(1));
                        return book;
                    }
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al crear el libro: " + book, e);
            throw new DataAccessException("Error al crear el libro.", e);
        }
        return null;
    }

    @Override
    public Book update(Book book) {
        try (Connection conn = DatabaseConnector.getConnection()){
            return updateWithConnection(book, conn);
        } catch(SQLException e) {
            LoggerUtil.logError("Error de SQL al actualizar el libro: " + book.getId(), e);
            throw new DataAccessException("Error al actualizar el libro.", e);
        }
    }

    public Book updateWithConnection(Book book, Connection conn) throws SQLException {
        String sql = "UPDATE libros SET isbn = ?, titulo = ?, autor = ?, editorial = ?, anio_publicacion = ?, genero = ?, stock_total = ?, stock_disponible = ? WHERE id_libro = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitulo());
            pstmt.setString(3, book.getAutor());
            pstmt.setString(4, book.getEditorial());

            if (book.getAnioPublicacion() != null) {
                pstmt.setInt(5, book.getAnioPublicacion());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            pstmt.setString(6, book.getGenero());
            pstmt.setInt(7, book.getStockTotal());
            pstmt.setInt(8, book.getStockDisponible());
            pstmt.setInt(9, book.getId());

            if (pstmt.executeUpdate() > 0) {
                return book;
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
    public Optional<Book> searchById(Integer id) {
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
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSetToLibro(rs));
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al obtener todos los libros.", e);
            throw new DataAccessException("Error al listar todos los libros.", e);
        }
        return books;
    }


    private Book mapResultSetToLibro(ResultSet rs) throws SQLException {
        return new Book(
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
