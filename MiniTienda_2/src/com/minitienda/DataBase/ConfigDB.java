package com.minitienda.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigDB {
    // Conexión a la base de datos( la inicializmos en null para que no aparezcan error al principio)
    private static Connection Connection = null;

    // Credenciales de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/mi_tienda";
    private static final String USER = "zapata";
    private static final String PASSWORD = "samuel1207@1207";

    // Método para obtener la conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        // try para manejar mejor los errores al iniciar con la conexion
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
                throw new SQLException("Error: Driver de MySQL no encontrado.", e);
            }
    }

    public static void closeConn() {
        // Cerrar la conexión a la base de datos
        try {
            if (Connection != null && !Connection.isClosed()) {
                Connection.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
