package org.example.dao;

import lombok.Getter;

import org.example.config.DatabaseConfig;
import org.example.model.Audit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Класс AuditDao отвечает за регистрацию действий пользователей
 */
@Getter
public class AuditDao {

    private static final String INSERT_AUDIT_SQL = "INSERT INTO audit (username, action, timestamp) VALUES (?, ?, ?)";

    /**
     * Регистрирует событие аудита
     *
     * @param audit Событие аудита
     */
    public void logEvent(Audit audit) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_AUDIT_SQL)) {
            statement.setString(1, audit.getUsername());
            statement.setString(2, audit.getAction());
            statement.setObject(3, audit.getTimestamp());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
