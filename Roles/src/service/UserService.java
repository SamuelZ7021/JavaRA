package service;

import config.Conexion;
import model.Administrador;
import model.Cliente;
import model.User;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de usuarios adaptada a la base de datos normalizada.
 * Utiliza transacciones y JOINs para gestionar la información en las tablas relacionadas.
 */
public class UserService implements UsuarioInterface {

    @Override
    public void registrarUsuario(User usuario) {
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false); // Inicia la transacción.

            String sqlUsuario = "INSERT INTO usuarios (nombre, email, password, rol_id, estado) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmtUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                pstmtUsuario.setString(1, usuario.getName());
                pstmtUsuario.setString(2, usuario.getEmail());
                pstmtUsuario.setString(3, usuario.getPassword());
                pstmtUsuario.setBoolean(5, usuario.isEstado());

                int rolId = (usuario instanceof Administrador) ? 1 : 2; // 1:ADMIN, 2:CLIENTE
                pstmtUsuario.setInt(4, rolId);

                pstmtUsuario.executeUpdate();

                try (ResultSet generatedKeys = pstmtUsuario.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int usuarioId = generatedKeys.getInt(1);
                        usuario.setId(usuarioId);

                        if (usuario instanceof Cliente) {
                            String sqlCliente = "INSERT INTO clientes (usuario_id, telefono, direccion) VALUES (?, ?, ?)";
                            try (PreparedStatement pstmtCliente = conn.prepareStatement(sqlCliente)) {
                                Cliente cliente = (Cliente) usuario;
                                pstmtCliente.setInt(1, usuarioId);
                                pstmtCliente.setString(2, cliente.getTelefono());
                                pstmtCliente.setString(3, cliente.getDireccion());
                                pstmtCliente.executeUpdate();
                            }
                        }
                    } else {
                        throw new SQLException("No se pudo obtener el ID del usuario creado.");
                    }
                }
            }
            conn.commit(); // Confirma la transacción.
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { /* Ignorar error de rollback */ }
            }
            if (e.getErrorCode() != 1062) { // No mostrar error para duplicados en datos de prueba
                JOptionPane.showMessageDialog(null, "Error de base de datos al registrar: " + e.getMessage());
            }
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { /* Ignorar error de cierre */ }
            }
        }
    }

    @Override
    public User iniciarSesion(String email, String password) {
        String sql = "SELECT u.id, u.nombre, u.email, u.password, u.estado, r.nombre_rol, c.telefono, c.direccion " +
                     "FROM usuarios u " +
                     "JOIN roles r ON u.rol_id = r.id " +
                     "LEFT JOIN clientes c ON u.id = c.usuario_id " +
                     "WHERE u.email = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                boolean estado = rs.getBoolean("estado");

                if (new Administrador("", "", storedPassword, estado).login(password)) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String nombreRol = rs.getString("nombre_rol");
                    User usuario;
                    if ("CLIENTE".equals(nombreRol)) {
                        usuario = new Cliente(nombre, email, storedPassword, estado, rs.getString("direccion"), rs.getString("telefono"));
                    } else {
                        usuario = new Administrador(nombre, email, storedPassword, estado);
                    }
                    usuario.setId(id);
                    return usuario;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos al iniciar sesión: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> obtenerTodosLosUsuarios() {
        List<User> listaUsuarios = new ArrayList<>();
        String sql = "SELECT u.id, u.nombre, u.email, u.password, u.estado, r.nombre_rol, c.telefono, c.direccion " +
                     "FROM usuarios u JOIN roles r ON u.rol_id = r.id LEFT JOIN clientes c ON u.id = c.usuario_id";
        try (Connection conn = Conexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User usuario;
                if ("CLIENTE".equals(rs.getString("nombre_rol"))) {
                    usuario = new Cliente(rs.getString("nombre"), rs.getString("email"), rs.getString("password"), rs.getBoolean("estado"), rs.getString("direccion"), rs.getString("telefono"));
                } else {
                    usuario = new Administrador(rs.getString("nombre"), rs.getString("email"), rs.getString("password"), rs.getBoolean("estado"));
                }
                usuario.setId(rs.getInt("id"));
                listaUsuarios.add(usuario);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos al obtener usuarios: " + e.getMessage());
        }
        return listaUsuarios;
    }

    @Override
    public User obtenerUsuarioPorId(int id) {
        String sql = "SELECT u.id, u.nombre, u.email, u.password, u.estado, r.nombre_rol, c.telefono, c.direccion " +
                     "FROM usuarios u JOIN roles r ON u.rol_id = r.id LEFT JOIN clientes c ON u.id = c.usuario_id WHERE u.id = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User usuario;
                if ("CLIENTE".equals(rs.getString("nombre_rol"))) {
                    usuario = new Cliente(rs.getString("nombre"), rs.getString("email"), rs.getString("password"), rs.getBoolean("estado"), rs.getString("direccion"), rs.getString("telefono"));
                } else {
                    usuario = new Administrador(rs.getString("nombre"), rs.getString("email"), rs.getString("password"), rs.getBoolean("estado"));
                }
                usuario.setId(rs.getInt("id"));
                return usuario;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void actualizarUsuario(User usuario) {
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false);
            String sqlUsuario = "UPDATE usuarios SET nombre = ?, password = ?, estado = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario)) {
                pstmt.setString(1, usuario.getName());
                pstmt.setString(2, usuario.getPassword());
                pstmt.setBoolean(3, usuario.isEstado());
                pstmt.setInt(4, usuario.getId());
                pstmt.executeUpdate();
            }
            if (usuario instanceof Cliente) {
                String sqlCliente = "UPDATE clientes SET telefono = ?, direccion = ? WHERE usuario_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlCliente)) {
                    Cliente cliente = (Cliente) usuario;
                    pstmt.setString(1, cliente.getTelefono());
                    pstmt.setString(2, cliente.getDireccion());
                    pstmt.setInt(3, cliente.getId());
                    pstmt.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { /* Ignorar */ } }
            JOptionPane.showMessageDialog(null, "Error de base de datos al actualizar: " + e.getMessage());
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { /* Ignorar */ } }
        }
    }

    @Override
    public void eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = Conexion.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos al eliminar: " + e.getMessage());
        }
    }

    public void gestionarBloqueoUsuario(Administrador admin, User usuarioAfectado, boolean bloquear) {
        if (admin.getId() == usuarioAfectado.getId()) {
            JOptionPane.showMessageDialog(null, "Un administrador no puede bloquearse a sí mismo.");
            return;
        }
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false);
            String sqlUpdate = "UPDATE usuarios SET estado = ? WHERE id = ?";
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                pstmtUpdate.setBoolean(1, !bloquear);
                pstmtUpdate.setInt(2, usuarioAfectado.getId());
                pstmtUpdate.executeUpdate();
            }
            String sqlBitacora = "INSERT INTO bitacora_bloqueos (admin_id, usuario_afectado_id, accion) VALUES (?, ?, ?)";
            try (PreparedStatement pstmtBitacora = conn.prepareStatement(sqlBitacora)) {
                pstmtBitacora.setInt(1, admin.getId());
                pstmtBitacora.setInt(2, usuarioAfectado.getId());
                pstmtBitacora.setString(3, bloquear ? "BLOQUEO" : "DESBLOQUEO");
                pstmtBitacora.executeUpdate();
            }
            conn.commit();
            JOptionPane.showMessageDialog(null, "Estado del usuario actualizado correctamente.");
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { /* Ignorar */ } }
            JOptionPane.showMessageDialog(null, "Error de base de datos al gestionar bloqueo: " + e.getMessage());
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { /* Ignorar */ } }
        }
    }
}
