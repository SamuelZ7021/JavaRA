package com.bliNva.dao.impl;

import com.bliNva.dao.UsuarioDAO;
import com.bliNva.exception.DataAccessException;
import com.bliNva.model.Usuario;
import com.bliNva.util.DatabaseConnector;
import com.bliNva.util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario create(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, apellido, email, password_hash, id_rol) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApellido());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getPasswordHash());
            pstmt.setInt(5, usuario.getIdRol());

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getInt(1));
                        return usuario;
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
    public Usuario update(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, email = ?, id_rol = ? WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApellido());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setInt(4, usuario.getIdRol());
            pstmt.setInt(5, usuario.getId());

            if (pstmt.executeUpdate() > 0) {
                return usuario;
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al actualizar el usuario con ID: " + usuario.getId(), e);
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
    public Optional<Usuario> searchById(Integer id) {
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
    public List<Usuario> getAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error de SQL al listar todos los usuarios.", e);
            throw new DataAccessException("Error al obtener la lista de usuarios.", e);
        }
        return usuarios;
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
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

    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
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

