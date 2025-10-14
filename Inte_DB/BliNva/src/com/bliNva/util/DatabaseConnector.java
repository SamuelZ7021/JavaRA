package com.bliNva.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseConnector {


    public static Connection getConnection() throws SQLException {
        String url = PropertiesLoader.getProperty("db.url");
        String user = PropertiesLoader.getProperty("db.user");
        String password = PropertiesLoader.getProperty("db.password");


        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);

        props.setProperty("prepareThreshold", "0");

        return DriverManager.getConnection(url, props);
    }
}

