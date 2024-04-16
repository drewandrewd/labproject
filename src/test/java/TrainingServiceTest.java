import org.example.service.TrainingService;
import org.example.service.UserService;
import org.example.model.Training;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты класса TrainingController")
public class TrainingServiceTest {

    private TrainingService trainingService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        trainingService = new TrainingService(userService);
        userService.register("testUser", "password");
        userService.login("testUser", "password");
    }

    @Test
    @DisplayName("Добавление тренировки")
    void addTraining_shouldAddTraining() {
        String dateTime = "10:00 10-10-2010";
        String trainingType = "Бег";
        int durationMinutes = 30;
        int burnedCalories = 200;
        String additionalInformation = "Утро";
        trainingService.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime);
        List<Training> userTrainings = trainingService.getTrainingRepository().getAllTrainings();
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
        trainingService.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime);
        trainingService.editTraining(0, "Плаванье", 45, 300, "Вечер", dateTime);
        List<Training> trainings = trainingService.getTrainingRepository().getAllTrainings();
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
        trainingService.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime);
        trainingService.deleteTraining(userService.getUser(), 0);
        List<Training> trainings = trainingService.getTrainingRepository().getAllTrainings();
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
        trainingService.getUserService().setCurrentUser(user);
        trainingService.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime);
        List<Training> userTrainings = trainingService.getUserTrainings(user);
        assertFalse(userTrainings.isEmpty());
    }

    @Test
    @DisplayName("Получение среднего количества калорий в минуту")
    void getAverageCaloriesPerMinute_shouldReturnZeroIfNoTrainings() {
        userService.setCurrentUser(new User("testUser", "password"));
        double avgCaloriesPerMinute = trainingService.getAverageCaloriesPerMinute();
        assertEquals(0, avgCaloriesPerMinute);
    }
}
