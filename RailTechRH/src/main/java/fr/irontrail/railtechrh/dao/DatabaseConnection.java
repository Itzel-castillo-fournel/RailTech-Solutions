package fr.irontrail.railtechrh.dao;


import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mariadb://localhost:3307/railtechrh";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}