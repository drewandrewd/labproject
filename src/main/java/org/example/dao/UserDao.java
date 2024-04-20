package org.example.dao;

import lombok.Data;
import org.example.config.DatabaseConfig;
import org.example.model.User;

import java.sql.*;
import java.util.*;

/**
 * Класс UserDao отвечает за хранение и управление данными пользователей.
 */
@Data
public class UserDao {

    private static final String INSERT_USER_SQL = "INSERT INTO users (username, password) VALUES (?, ?)";
    private static final String SELECT_USER_SQL = "SELECT * FROM users WHERE username = ? AND password = ?";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM users";
    private static final String SELECT_USER_BY_NAME_SQL = "SELECT * FROM users WHERE username = ?";

    /**
     * Регистрирует нового пользователя.
     *
     * @param user пользователь для регистрации
     */
    public void register(User user) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                user.setId(id);
            } else {
                throw new SQLException("Failed to get generated key.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.out.println("Пользователь с таким именем уже существует.");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * Аутентифицирует пользователя.
     *
     * @param name     имя пользователя
     * @param password пароль пользователя
     * @return объект пользователя, если аутентификация прошла успешно, в противном случае null
     */
    public User login(String name, String password) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_SQL)) {
            statement.setString(1, name);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(resultSet.getLong("id"), resultSet.getString("username"), resultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return список всех пользователей
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_USERS_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("username");
                String password = resultSet.getString("password");
                users.add(new User(name, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Возвращает пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return объект пользователя, если пользователь существует, в противном случае null
     */
    public User getUserByUsername(String username) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_NAME_SQL)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    return new User(name, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

