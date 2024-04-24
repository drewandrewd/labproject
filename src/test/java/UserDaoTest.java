import org.example.config.DatabaseConfig;
import org.example.dao.UserDao;
import org.example.model.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {

    @Container
    private static final PostgreSQLContainer<?> databaseContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static UserDao userDao;

    @BeforeAll
    static void setUp() throws SQLException {
        String jdbcUrl = databaseContainer.getJdbcUrl();
        DatabaseConfig.setConnectionConfig(jdbcUrl, "test", "test");
        userDao = new UserDao();
        createUsersTable();
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

    @BeforeEach
    void setUpEach() throws SQLException {
        clearUsersTable();
    }

    private static void clearUsersTable() throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM users");
        }
    }

    @Test
    void registerUser() {
        User user = new User();
        user.setName("test_user");
        user.setPassword("password");

        userDao.register(user);

        User registeredUser = userDao.getUserByUsername("test_user");
        assertNotNull(registeredUser);
    }

    @Test
    void login() {
        User user = new User();
        user.setName("test_user");
        user.setPassword("password");

        userDao.register(user);

        User loggedInUser = userDao.login("test_user", "password");
        assertNotNull(loggedInUser);
    }

    @Test
    void getAllUsers() {
        User user = new User();
        user.setName("test_user");
        user.setPassword("password");

        userDao.register(user);

        List<User> users = userDao.getAllUsers();
        assertEquals(1, users.size());
    }

    @Test
    void getUserByUsername() {
        User user = new User();
        user.setName("test_user");
        user.setPassword("password");

        userDao.register(user);

        User retrievedUser = userDao.getUserByUsername("test_user");
        assertNotNull(retrievedUser);
    }
}
