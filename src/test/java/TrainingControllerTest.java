import org.example.in.TrainingController;
import org.example.in.UserController;
import org.example.model.Training;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты класса TrainingController")
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
    @DisplayName("Добавление тренировки")
    void addTraining_shouldAddTraining() {
        String dateTime = "10:00 10-10-2010";
        String trainingType = "Бег";
        int durationMinutes = 30;
        int burnedCalories = 200;
        String additionalInformation = "Утро";
        trainingController.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime);
        List<Training> userTrainings = trainingController.getTrainingRepository().getAllTrainings();
        assertFalse(userTrainings.isEmpty());
    }

    @Test
    @DisplayName("Редактирование тренировки")
    void editTraining_shouldEditTraining() {
        String dateTime = "10:00 10-10-2010";
        String trainingType = "Бег";
        int durationMinutes = 30;
        int burnedCalories = 200;
        String additionalInformation = "Утро";
        trainingController.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime);
        trainingController.editTraining(0, "Плаванье", 45, 300, "Вечер", dateTime);
        List<Training> trainings = trainingController.getTrainingRepository().getAllTrainings();
        assertEquals("Плаванье", trainings.get(0).getType());
    }

    @Test
    @DisplayName("Удаление тренировки")
    void deleteTraining_shouldDeleteTraining() {
        String dateTime = "10:00 10-10-2010";
        String trainingType = "Бег";
        int durationMinutes = 30;
        int burnedCalories = 200;
        String additionalInformation = "Утро";
        trainingController.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime);
        trainingController.deleteTraining(userController.getUser(), 0);
        List<Training> trainings = trainingController.getTrainingRepository().getAllTrainings();
        assertTrue(trainings.isEmpty());
    }

    @Test
    @DisplayName("Получение тренировок пользователя")
    void getUserTrainings_shouldReturnUserTrainings() {
        String dateTime = "10:00 10-10-2010";
        User user = new User("testUser", "password");
        String trainingType = "Бег";
        int durationMinutes = 30;
        int burnedCalories = 200;
        String additionalInformation = "Утро";
        trainingController.getUserController().setCurrentUser(user);
        trainingController.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime);
        List<Training> userTrainings = trainingController.getUserTrainings(user);
        assertFalse(userTrainings.isEmpty());
    }

    @Test
    @DisplayName("Получение среднего количества калорий в минуту")
    void getAverageCaloriesPerMinute_shouldReturnZeroIfNoTrainings() {
        userController.setCurrentUser(new User("testUser", "password"));
        double avgCaloriesPerMinute = trainingController.getAverageCaloriesPerMinute();
        assertEquals(0, avgCaloriesPerMinute);
    }
}
