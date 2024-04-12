import org.example.in.TrainingController;
import org.example.in.UserController;
import org.example.model.Training;
import org.example.model.User;
import org.example.repositories.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingControllerTest {

    private TrainingController trainingController;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        trainingController = new TrainingController(userController);
        userController.register("testUser", "password");
        userController.login("testUser", "password");
    }

    @Test
    void addTraining_shouldAddTraining() {
        LocalDateTime dateTime = LocalDateTime.now();
        String trainingType = "Бег";
        int durationMinutes = 30;
        int burnedCalories = 200;
        String additionalInformation = "Утро";
        trainingController.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime);
        List<Training> userTrainings = trainingController.getTrainingRepository().getAllTrainings();
        assertFalse(userTrainings.isEmpty());
    }

    @Test
    void editTraining_shouldEditTraining() {
        LocalDateTime dateTime = LocalDateTime.now();
        String trainingType = "Бег";
        int durationMinutes = 30;
        int burnedCalories = 200;
        String additionalInformation = "Утро";
        trainingController.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime);
        trainingController.editTraining(0, "Плаванье", 45, 300, "Вечер", LocalDateTime.now());
        List<Training> trainings = trainingController.getTrainingRepository().getAllTrainings();
        assertEquals("Плаванье", trainings.get(0).getType());
    }

    @Test
    void deleteTraining_shouldDeleteTraining() {
        LocalDateTime dateTime = LocalDateTime.now();
        String trainingType = "Бег";
        int durationMinutes = 30;
        int burnedCalories = 200;
        String additionalInformation = "Утро";
        trainingController.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime);
        trainingController.deleteTraining(0);
        List<Training> trainings = trainingController.getTrainingRepository().getAllTrainings();
        assertTrue(trainings.isEmpty());
    }

    @Test
    void getUserTrainings_shouldReturnUserTrainings() {
        LocalDateTime dateTime = LocalDateTime.now();
        User user = new User("testUser", "password");
        String trainingType = "Бег";
        int durationMinutes = 30;
        int burnedCalories = 200;
        String additionalInformation = "Утро";
        trainingController.getUserController().setCurrentUser(user);
        trainingController.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime);
        List<Training> userTrainings = trainingController.getUserTrainings();
        assertFalse(userTrainings.isEmpty());
    }

    @Test
    void getAverageCaloriesPerMinute_shouldReturnZeroIfNoTrainings() {
        userController.setCurrentUser(new User("testUser", "password"));
        double avgCaloriesPerMinute = trainingController.getAverageCaloriesPerMinute();
        assertEquals(0, avgCaloriesPerMinute);
    }
}
