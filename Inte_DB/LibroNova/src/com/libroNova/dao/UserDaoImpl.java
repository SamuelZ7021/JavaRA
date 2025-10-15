package com.libroNova.dao;


import com.libroNova.exception.DataAccessException;
import com.libroNova.interfas.UserDao;
import com.libroNova.model.User;
import com.libroNova.util.DatabaseConnector;
import com.libroNova.util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    @Override
    public User create(User user) {
        String sql = "INSERT INTO usuarios (nombre, apellido, email, password_hash, id_rol) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getNombre());
            pstmt.setString(2, user.getApellido());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPasswordHash());
            pstmt.setInt(5, user.getIdRol());

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al crear el usuario.", e);
            throw new DataAccessException("No se pudo crear el usuario.", e);
        }
        return null;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, email = ?, id_rol = ? WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getNombre());
            pstmt.setString(2, user.getApellido());
            pstmt.setString(3, user.getEmail());
            pstmt.setInt(4, user.getIdRol());
            pstmt.setInt(5, user.getId());

            if (pstmt.executeUpdate() > 0) {
                return user;
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al actualizar el usuario con ID: " + user.getId(), e);
            throw new DataAccessException("No se pudo actualizar el usuario.", e);
        }
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al eliminar el usuario con ID: " + id, e);
            throw new DataAccessException("No se pudo eliminar el usuario.", e);
        }
    }

    @Override
    public Optional<User> searchById(Integer id) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al buscar usuario por ID: " + id, e);
            throw new DataAccessException("Error al buscar usuario por ID.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al listar todos los usuarios.", e);
            throw new DataAccessException("Error al obtener la lista de usuarios.", e);
        }
        return users;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al buscar usuario por email: " + email, e);
            throw new DataAccessException("Error al buscar usuario por email.", e);
        }
        return Optional.empty();
    }

    private User mapResultSetToUsuario(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id_usuario"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getInt("id_rol"),
                rs.getTimestamp("fecha_creacion").toLocalDateTime()
        );
    }
}
