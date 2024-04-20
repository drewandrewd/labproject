import org.example.config.DatabaseConfig;
import org.example.dao.UserDao;
import org.example.model.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {

    @Container
    private static final GenericContainer<?> databaseContainer = new GenericContainer<>("postgres:latest")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB", "test")
            .withEnv("POSTGRES_USER", "test")
            .withEnv("POSTGRES_PASSWORD", "test");

    private static UserDao userDao;

    @BeforeAll
    static void setUp() throws SQLException {
        databaseContainer.start();
        DatabaseConfig.setConnectionConfig(getJdbcUrl(), "test", "test");
        userDao = new UserDao();
        createUsersTable();
    }

    @AfterAll
    static void tearDown() {
        databaseContainer.stop();
    }

    private static String getJdbcUrl() {
        return "jdbc:postgresql://localhost:" + databaseContainer.getMappedPort(5432) + "/test?currentSchema=public";
    }

    private static void createUsersTable() throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS users (" +
                             "id SERIAL PRIMARY KEY," +
                             "username VARCHAR(255) NOT NULL UNIQUE," +
                             "password VARCHAR(255) NOT NULL)")) {
            statement.executeUpdate();
        }
    }

    @Test
    @Order(1)
    void registerUser() {
        User user = new User();
        user.setName("test_user");
        user.setPassword("password");

        userDao.register(user);

        User registeredUser = userDao.getUserByUsername("test_user");
        assertNotNull(registeredUser);
    }

    @Test
    @Order(2)
    void login() {
        User user = userDao.login("test_user", "password");
        assertNotNull(user);
    }

    @Test
    @Order(3)
    void getAllUsers() {
        List<User> users = userDao.getAllUsers();
        assertEquals(1, users.size());
    }

    @Test
    @Order(4)
    void getUserByUsername() {
        User user = userDao.getUserByUsername("test_user");
        assertNotNull(user);
    }
}
