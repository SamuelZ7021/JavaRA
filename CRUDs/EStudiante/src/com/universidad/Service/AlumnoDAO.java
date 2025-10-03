package com.universidad.Service;

import com.universidad.model.Alumno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {
    public void agregarAlumno(Alumno alumno){
        String sql = "INSERT INTO alumnos (nombre, edad) VALUES (?, ?)";
        try (Connection conn = ConfigDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, alumno.getNombre());
            pstmt.setInt(2, alumno.getEdad());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    alumno.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Error al agregar alumno: " + e.getMessage(), e);
        }
    }

    public void eliminarAlumno(int id){
        String sql = "DELETE FROM alumnos WHERE id = ?";
        try (Connection conn = ConfigDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Error al eliminar alumno: " + e.getMessage(), e);
        }
    }
// hola
    public List<Alumno> obtenerTodosLosAlumnos(){
        List<Alumno> listaAlumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumnos";
        try (Connection conn = ConfigDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                Alumno alumno = new Alumno(rs.getString("nombre"), rs.getInt("edad"));
                alumno.setId(rs.getInt("id"));
                listaAlumnos.add(alumno);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en la base de datos al obtener alumnos: " + e.getMessage(), e);
        }
        return listaAlumnos;
    }
    
    public Alumno obtenerAlumnoPorId(int id){
        String sql = "SELECT * FROM alumnos WHERE id = ?";
        try (Connection conn = ConfigDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){
                    Alumno alumno = new Alumno(rs.getString("nombre"), rs.getInt("edad"));
                    alumno.setId(rs.getInt("id"));
                    return alumno;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener alumno por ID: " + e.getMessage(), e);
        }
        return null;
    }
}
