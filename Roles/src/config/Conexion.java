package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de utilidad para gestionar la conexión a la base de datos MySQL.
 */
public class Conexion {
    // URL de conexión a la base de datos.
    private static final String URL = "jdbc:mysql://localhost:3306/usuarios_db";

    // Credenciales de la base de datos.
    private static final String USER = "zapata";
    private static final String PASSWORD = "samuel1207@1207";

    // Establece y devuelve una conexión con la base de datos.
    public static Connection getConnection() throws SQLException {
        try {
            // Carga explícitamente el driver de MySQL.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Este error ocurre si el .jar del conector no está en el classpath.
            throw new SQLException("Error: Driver de MySQL no encontrado.", e);
        }
        // Intenta establecer la conexión y la devuelve.
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
