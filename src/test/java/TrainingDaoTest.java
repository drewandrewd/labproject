import org.example.config.DatabaseConfig;
import org.example.dao.TrainingDao;
import org.example.model.Training;
import org.example.model.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
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
    private static final PostgreSQLContainer<?> databaseContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static TrainingDao trainingDao;

    @BeforeAll
    static void setUp() throws SQLException {
        String jdbcUrl = databaseContainer.getJdbcUrl();
        DatabaseConfig.setConnectionConfig(jdbcUrl, "test", "test");
        trainingDao = new TrainingDao();
        createTrainingsTable();
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

    @BeforeEach
    void setUpEach() throws SQLException {
        clearTrainingsTable();
    }

    private static void clearTrainingsTable() throws SQLException {
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM trainings");
        }
    }

    @Test
    void addTraining() {
        User user = new User();
        user.setId(1L);

        Training training = new Training();
        training.setType("Running");
        training.setDurationMinutes(30);
        training.setBurnedCalories(300);
        training.setAdditionalInformation("Morning run");
        training.setDateTime(LocalDateTime.of(2024, 4, 20, 8, 0)); // изменена дата
        training.setUser(user);

        trainingDao.addTraining(training);

        List<Training> trainings = trainingDao.getUserTrainings(user);
        assertEquals(1, trainings.size());
    }

    @Test
    void getUserTrainings() {
        User user = new User();
        user.setId(1L);

        Training training = new Training();
        training.setType("Running");
        training.setDurationMinutes(30);
        training.setBurnedCalories(300);
        training.setAdditionalInformation("Morning run");
        training.setDateTime(LocalDateTime.of(2024, 4, 20, 8, 0)); // изменена дата
        training.setUser(user);

        trainingDao.addTraining(training);

        List<Training> trainings = trainingDao.getUserTrainings(user);
        assertEquals(1, trainings.size());
    }

    @Test
    void getAllTrainings() {
        User user = new User();
        user.setId(1L);

        Training training = new Training();
        training.setType("Running");
        training.setDurationMinutes(30);
        training.setBurnedCalories(300);
        training.setAdditionalInformation("Morning run");
        training.setDateTime(LocalDateTime.of(2024, 4, 20, 8, 0)); // изменена дата
        training.setUser(user);

        trainingDao.addTraining(training);

        List<Training> trainings = trainingDao.getAllTrainings();
        assertEquals(1, trainings.size());
    }

    @Test
    void editTraining() {
        User user = new User();
        user.setId(1L);

        Training training = new Training();
        training.setType("Running");
        training.setDurationMinutes(30);
        training.setBurnedCalories(300);
        training.setAdditionalInformation("Morning run");
        training.setDateTime(LocalDateTime.of(2024, 4, 20, 8, 0)); // изменена дата
        training.setUser(user);

        trainingDao.addTraining(training);

        List<Training> trainings = trainingDao.getUserTrainings(user);
        Training trainingToUpdate = trainings.get(0);

        trainingToUpdate.setType("Swimming");
        trainingToUpdate.setDurationMinutes(45);
        trainingToUpdate.setBurnedCalories(500);
        trainingToUpdate.setAdditionalInformation("Evening swimming");
        trainingToUpdate.setDateTime(LocalDateTime.of(2024, 4, 20, 18, 0)); // изменена дата

        trainingDao.editTraining(trainingToUpdate.getId(), trainingToUpdate.getType(), trainingToUpdate.getDurationMinutes(),
                trainingToUpdate.getBurnedCalories(), trainingToUpdate.getAdditionalInformation(), trainingToUpdate.getDateTime());

        Training updatedTraining = trainingDao.getUserTrainings(user).get(0);
        assertEquals("Swimming", updatedTraining.getType());
        assertEquals(45, updatedTraining.getDurationMinutes());
        assertEquals(500, updatedTraining.getBurnedCalories());
        assertEquals("Evening swimming", updatedTraining.getAdditionalInformation());
        assertEquals(LocalDateTime.of(2024, 4, 20, 18, 0), updatedTraining.getDateTime()); // изменена дата
    }

    @Test
    void deleteTraining() {
        User user = new User();
        user.setId(1L);

        Training training = new Training();
        training.setType("Running");
        training.setDurationMinutes(30);
        training.setBurnedCalories(300);
        training.setAdditionalInformation("Morning run");
        training.setDateTime(LocalDateTime.of(2024, 4, 20, 8, 0)); // изменена дата
        training.setUser(user);

        trainingDao.addTraining(training);

        List<Training> trainings = trainingDao.getUserTrainings(user);
        Training trainingToDelete = trainings.get(0);

        trainingDao.deleteTraining(trainingToDelete.getId());

        List<Training> remainingTrainings = trainingDao.getUserTrainings(user);
        assertEquals(0, remainingTrainings.size());
    }
}
