package com.universidad.Service;

import com.universidad.model.Asignatura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsignaturaDAO {

    public void agregarAsignaturaAAlumno(Asignatura asignatura, int idAlumno) {
        String sql = "INSERT INTO asignaturas (nombre, nota, alumno_id) VALUES (?, ?, ?)";
        try (Connection conn = ConfigDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, asignatura.getAsigNombre());
            pstmt.setDouble(2, asignatura.getNota());
            pstmt.setInt(3, idAlumno);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    asignatura.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al agregar asignatura: " + e.getMessage(), e);
        }
    }

    public List<Asignatura> obtenerAsignaturasDeAlumno(int idAlumno) {
        List<Asignatura> asignaturas = new ArrayList<>();
        String sql = "SELECT * FROM asignaturas WHERE alumno_id = ?";
        try (Connection conn = ConfigDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAlumno);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Asignatura asignatura = new Asignatura(
                            rs.getString("nombre"),
                            rs.getDouble("nota")
                    );
                    asignatura.setId(rs.getInt("id"));
                    asignaturas.add(asignatura);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener asignaturas: " + e.getMessage(), e);
        }
        return asignaturas;
    }

    public void eliminarAsignatura(int idAsignatura) {
        String sql = "DELETE FROM asignaturas WHERE id = ?";
        try (Connection conn = ConfigDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAsignatura);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar asignatura: " + e.getMessage(), e);
        }
    }
}
