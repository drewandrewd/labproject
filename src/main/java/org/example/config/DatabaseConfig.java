package org.example.config;

import lombok.Data;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Data
@Setter
public class DatabaseConfig {

    private static String URL = "jdbc:postgresql://localhost:5433/postgres?currentSchema=my_schema";
    private static String USER = "postgres";
    private static String PASSWORD = "password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void setConnectionConfig(String url, String user, String password) {
        URL = url;
        USER = user;
        PASSWORD = password;
    }
}
