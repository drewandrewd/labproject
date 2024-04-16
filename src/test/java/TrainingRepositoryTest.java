import org.example.model.Training;
import org.example.model.User;
import org.example.repositories.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тесты класса TrainingRepository")
public class TrainingRepositoryTest {

    private TrainingRepository trainingRepository;
    private User testUser;
    private Training testTraining;

    @BeforeEach
    void setUp() {
        trainingRepository = new TrainingRepository();
        testUser = new User("testUser", "password");
        testTraining = new Training("type", 60, 200, "additionalInformation", LocalDateTime.now(), testUser);
    }

    @Test
    @DisplayName("Добавление тренировки")
    void addTraining_AddTrainingToRepository() {
        trainingRepository.addTraining(testTraining);
        List<Training> allTrainings = trainingRepository.getAllTrainings();
        assertTrue(allTrainings.contains(testTraining));
    }

    @Test
    @DisplayName("Получение тренировок пользователя")
    void getUserTrainings_ReturnCorrectTrainingsForUser() {
        trainingRepository.addTraining(testTraining);
        List<Training> userTrainings = trainingRepository.getUserTrainings(testUser);
        assertTrue(userTrainings.contains(testTraining));
    }

    @Test
    @DisplayName("Редактирование тренировки")
    void editTraining_shouldEditTrainingCorrectly() {
        trainingRepository.addTraining(testTraining);
        Training modifiedTraining = new Training("newType", 90, 300, "newAdditionalInformation", LocalDateTime.now(), testUser);
        trainingRepository.editTraining(testUser,0, modifiedTraining.getType(), modifiedTraining.getDurationMinutes(),
                modifiedTraining.getBurnedCalories(), modifiedTraining.getAdditionalInformation(), modifiedTraining.getDateTime());
        Training updatedTraining = trainingRepository.getUserTrainings(testUser).get(0);
        assertEquals(modifiedTraining, updatedTraining);
    }

    @Test
    @DisplayName("Удаление тренировки")
    void deleteTraining_shouldDeleteTrainingFromRepository() {
        trainingRepository.addTraining(testTraining);
        trainingRepository.deleteTraining(testUser,0);
        List<Training> allTrainings = trainingRepository.getAllTrainings();
        assertTrue(allTrainings.isEmpty());
    }
}
