package com.example.estate_agency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    private static final String dbName = "real_estate_agency";
    private static final String userName = "java";
    private static final String password = "3355example33"; // Укажите пароль, если он установлен

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}