package org.example.config;

import liquibase.resource.Resource;
import lombok.Data;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Data
@Setter
public class  DatabaseConfig {

    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String LIQUIBASE_CHANGELOG;

    static {
        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadConfig() throws IOException {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("src/main/resources/application.yml")) {
            prop.load(input);
            URL = prop.getProperty("url");
            USER = prop.getProperty("username");
            PASSWORD = prop.getProperty("password");
            LIQUIBASE_CHANGELOG = prop.getProperty("liquibaseChangeLog");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static String getLiquibaseChangeLog() {
        return LIQUIBASE_CHANGELOG;
    }

    public static void setConnectionConfig(String url, String user, String password) {
        URL = url;
        USER = user;
        PASSWORD = password;
    }
}
