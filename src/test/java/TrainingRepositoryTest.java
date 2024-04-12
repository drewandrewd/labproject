import org.example.model.Training;
import org.example.model.User;
import org.example.repositories.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void addTraining_AddTrainingToRepository() {
        trainingRepository.addTraining(testTraining);
        List<Training> allTrainings = trainingRepository.getAllTrainings();
        assertTrue(allTrainings.contains(testTraining));
    }

    @Test
    void getUserTrainings_ReturnCorrectTrainingsForUser() {
        trainingRepository.addTraining(testTraining);
        List<Training> userTrainings = trainingRepository.getUserTrainings(testUser);
        assertTrue(userTrainings.contains(testTraining));
    }

    @Test
    void editTraining_shouldEditTrainingCorrectly() {
        trainingRepository.addTraining(testTraining);
        Training modifiedTraining = new Training("newType", 90, 300, "newAdditionalInformation", LocalDateTime.now(), testUser);
        trainingRepository.editTraining(0, modifiedTraining.getType(), modifiedTraining.getDurationMinutes(),
                modifiedTraining.getBurnedCalories(), modifiedTraining.getAdditionalInformation(), modifiedTraining.getDateTime());
        Training updatedTraining = trainingRepository.getTrainingById(0);
        assertEquals(modifiedTraining, updatedTraining);
    }

    @Test
    void deleteTraining_shouldDeleteTrainingFromRepository() {
        trainingRepository.addTraining(testTraining);
        trainingRepository.deleteTraining(0);
        List<Training> allTrainings = trainingRepository.getAllTrainings();
        assertTrue(allTrainings.isEmpty());
    }
}
