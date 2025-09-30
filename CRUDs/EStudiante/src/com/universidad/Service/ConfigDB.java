package com.universidad.Service;

import java.sql.Connection;
import java.sql.SQLException;

public class ConfigDB {

    public static final String URL = "jdbc:mysql://localhost:3306/universidad_db";
    public static final String USER = "zapata";
    public static final String PASSWORD = "samuel1207@1207";

    public static Connection getConnection() throws SQLException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e){
            throw new SQLException("Error: Driver de MySQL no encontrado.", e);
        }
        return java.sql.DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
