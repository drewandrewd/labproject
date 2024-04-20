import org.example.config.DatabaseConfig;
import org.example.dao.AuditDao;
import org.example.model.Audit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuditDaoTest {

    @Container
    private static final GenericContainer<?> databaseContainer = new GenericContainer<>("postgres:latest")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB", "test")
            .withEnv("POSTGRES_USER", "test")
            .withEnv("POSTGRES_PASSWORD", "test");

    private static AuditDao auditDao;

    @BeforeAll
    static void setUp() throws SQLException {
        databaseContainer.start();
        String jdbcUrl = "jdbc:postgresql://" + databaseContainer.getHost() + ":" + databaseContainer.getFirstMappedPort() + "/test";
        DatabaseConfig.setConnectionConfig(jdbcUrl, "test", "test");
        auditDao = new AuditDao();
        createAuditTable();
    }

    @AfterAll
    static void tearDown() {
        databaseContainer.stop();
    }

    private static void createAuditTable() throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS audit (" +
                             "id SERIAL PRIMARY KEY," +
                             "username VARCHAR(255) NOT NULL," +
                             "action VARCHAR(255) NOT NULL," +
                             "timestamp TIMESTAMP NOT NULL)")) {
            statement.executeUpdate();
        }
    }

    @Test
    void logEvent() throws SQLException {
        String username = "test_user";
        String action = "test_action";
        LocalDateTime timestamp = LocalDateTime.now();
        Audit audit = new Audit(username, action, timestamp);

        auditDao.logEvent(audit);

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM audit WHERE username = ? AND action = ? AND timestamp = ?")) {
            statement.setString(1, username);
            statement.setString(2, action);
            statement.setObject(3, timestamp);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    assertEquals(username, resultSet.getString("username"));
                    assertEquals(action, resultSet.getString("action"));
                    assertEquals(timestamp, resultSet.getObject("timestamp", LocalDateTime.class));
                }
            }
        }
    }
}
