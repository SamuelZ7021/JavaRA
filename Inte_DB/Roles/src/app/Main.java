package app;

import config.Conexion;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static Conexion conexion;

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.mostrarMenu();

                System.out.println("--- Iniciando prueba de conexión a la base de datos ---");

                Connection connection = null;
                try {
                    // 1. Intentamos obtener una conexión
                    connection = conexion.getConnection();

                    // 2. Si la línea anterior no lanzó un error, la conexión fue exitosa
                    if (connection != null && !connection.isClosed()) {
                        System.out.println("***************************************************");
                        System.out.println("* ¡CONEXIÓN EXITOSA!                              *");
                        System.out.println("* Tu aplicación puede comunicarse con MySQL.      *");
                        System.out.println("***************************************************");
                    }

                } catch (SQLException e) {
                    // 3. Si algo salió mal (URL incorrecta, usuario/pass inválido, DB no existe)...
                    System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.err.println("! ERROR AL CONECTAR A LA BASE DE DATOS              !");
                    System.err.println("! Por favor, revisa los siguientes puntos:          !");
                    System.err.println("! 1. ¿Está el servidor MySQL en ejecución?          !");
                    System.err.println("! 2. ¿Son correctos el usuario y la contraseña en conexion.java? !");
                    System.err.println("! 3. ¿Existe la base de datos 'usuarios_db'?        !");
                    System.err.println("! 4. ¿Está el 'Connector/J' (.jar) en el build path del proyecto? !");
                    System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    // Imprimimos el detalle técnico del error
                    e.printStackTrace();

                } finally {
                    // 4. Cerramos la conexión para liberar recursos, sin importar si hubo éxito o error.
                    if (connection != null) {
                        try {
                            connection.close();
                            System.out.println("--- Conexión cerrada correctamente. ---");
                        } catch (SQLException e) {
                            System.err.println("Error al cerrar la conexión: " + e.getMessage());
                        }
                    }
                }
    }
}