import org.example.config.DatabaseConfig;
import org.example.dao.TrainingDao;
import org.example.model.Training;
import org.example.model.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainingDaoTest {

    @Container
    private static final GenericContainer<?> databaseContainer = new GenericContainer<>("postgres:latest")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB", "test")
            .withEnv("POSTGRES_USER", "test")
            .withEnv("POSTGRES_PASSWORD", "test");

    private static TrainingDao trainingDao;

    @BeforeAll
    static void setUp() throws SQLException {
        databaseContainer.start();
        DatabaseConfig.setConnectionConfig(getJdbcUrl(), "test", "test");
        trainingDao = new TrainingDao();
        createTrainingsTable();
    }

    @AfterAll
    static void tearDown() {
        databaseContainer.stop();
    }

    private static String getJdbcUrl() {
        return "jdbc:postgresql://localhost:" + databaseContainer.getMappedPort(5432) + "/test?currentSchema=public";
    }

    private static void createTrainingsTable() throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS trainings (" +
                             "id SERIAL PRIMARY KEY," +
                             "type VARCHAR(255) NOT NULL," +
                             "duration_minutes INT NOT NULL," +
                             "calories_burned INT NOT NULL," +
                             "additional_information VARCHAR(255)," +
                             "datetime TIMESTAMP NOT NULL," +
                             "user_id BIGINT NOT NULL)")) {
            statement.executeUpdate();
        }
    }

    @Test
    @Order(1)
    void addTraining() {
        User user = new User();
        user.setId(1L);

        Training training = new Training();
        training.setType("Running");
        training.setDurationMinutes(30);
        training.setBurnedCalories(300);
        training.setAdditionalInformation("Morning run");
        training.setDateTime(LocalDateTime.of(2022, 4, 20, 8, 0));
        training.setUser(user);

        trainingDao.addTraining(training);

        List<Training> trainings = trainingDao.getUserTrainings(user);
        assertEquals(1, trainings.size());
    }

    @Test
    @Order(2)
    void getUserTrainings() {
        User user = new User();
        user.setId(1L);

        List<Training> trainings = trainingDao.getUserTrainings(user);
        assertEquals(1, trainings.size());
    }

    @Test
    @Order(3)
    void getAllTrainings() {
        List<Training> trainings = trainingDao.getAllTrainings();
        assertEquals(1, trainings.size());
    }

    @Test
    @Order(4)
    void editTraining() {
        User user = new User();
        user.setId(1L);

        List<Training> trainings = trainingDao.getUserTrainings(user);
        Training training = trainings.get(0);

        training.setType("Swimming");
        training.setDurationMinutes(45);
        training.setBurnedCalories(500);
        training.setAdditionalInformation("Evening swimming");
        training.setDateTime(LocalDateTime.of(2022, 4, 20, 18, 0));

        trainingDao.editTraining(training.getId(), training.getType(), training.getDurationMinutes(),
                training.getBurnedCalories(), training.getAdditionalInformation(), training.getDateTime());

        Training updatedTraining = trainingDao.getUserTrainings(user).get(0);
        assertEquals("Swimming", updatedTraining.getType());
        assertEquals(45, updatedTraining.getDurationMinutes());
        assertEquals(500, updatedTraining.getBurnedCalories());
        assertEquals("Evening swimming", updatedTraining.getAdditionalInformation());
        assertEquals(LocalDateTime.of(2022, 4, 20, 18, 0), updatedTraining.getDateTime());
    }

    @Test
    @Order(5)
    void deleteTraining() {
        User user = new User();
        user.setId(1L);

        List<Training> trainings = trainingDao.getUserTrainings(user);
        Training training = trainings.get(0);

        trainingDao.deleteTraining(training.getId());

        List<Training> remainingTrainings = trainingDao.getUserTrainings(user);
        assertEquals(0, remainingTrainings.size());
    }
}
