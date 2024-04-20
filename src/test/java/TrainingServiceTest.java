import org.example.dao.AuditDao;
import org.example.dao.TrainingDao;
import org.example.model.Training;
import org.example.model.User;
import org.example.service.AuditService;
import org.example.service.TrainingService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private UserService userService;

    @Mock
    private AuditService auditService;

    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainingService = new TrainingService(userService);
        trainingService.setTrainingDao(trainingDao);
        trainingService.setAuditService(auditService);
    }

    @Test
    void addTraining() {
        String trainingType = "Running";
        int durationMinutes = 30;
        int burnedCalories = 200;
        String additionalInformation = "Morning run";
        String date = "08:00 20-04-2024";
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));

        when(userService.getUser()).thenReturn(new User("test_user", "password"));

        trainingService.addTraining(trainingType, durationMinutes, burnedCalories, additionalInformation, date);

        verify(trainingDao, times(1)).addTraining(any());
        verify(auditService, times(1)).logTrainingAdded("test_user", trainingType);
    }

    @Test
    void viewTrainings() {
        User user = new User("test_user", "password");
        List<Training> userTrainings = new ArrayList<>();
        userTrainings.add(new Training("Running", 30, 200, "Morning run", LocalDateTime.of(2024, 4, 20, 8, 0), user));

        when(userService.getUser()).thenReturn(user);
        when(trainingDao.getUserTrainings(user)).thenReturn(userTrainings);

        trainingService.viewTrainings(user);

        verify(trainingDao, times(1)).getUserTrainings(user);
    }

    @Test
    void editTraining() {
        long trainingId = 1;
        String type = "Swimming";
        int durationMinutes = 45;
        int burnedCalories = 300;
        String additionalInformation = "Evening swimming";
        String date = "18:00 20-04-2024";
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));

        when(userService.getUser()).thenReturn(new User("test_user", "password"));

        trainingService.editTraining(trainingId, type, durationMinutes, burnedCalories, additionalInformation, date);

        verify(trainingDao, times(1)).editTraining(trainingId, type, durationMinutes, burnedCalories, additionalInformation, dateTime);
        verify(auditService, times(1)).logTrainingEdited("test_user", type);
    }

    @Test
    void deleteTraining() {
        long trainingId = 1;

        trainingService.deleteTraining(trainingId);

        verify(trainingDao, times(1)).deleteTraining(trainingId);
    }

    @Test
    void getUserTrainings() {
        User user = new User("test_user", "password");
        List<Training> userTrainings = new ArrayList<>();
        userTrainings.add(new Training("Running", 30, 200, "Morning run", LocalDateTime.of(2024, 4, 20, 8, 0), user));

        when(trainingDao.getUserTrainings(user)).thenReturn(userTrainings);

        List<Training> result = trainingService.getUserTrainings(user);

        verify(trainingDao, times(1)).getUserTrainings(user);
    }

    @Test
    void getAverageCaloriesPerMinute() {
        User user = new User("test_user", "password");
        List<Training> userTrainings = new ArrayList<>();
        userTrainings.add(new Training("Running", 30, 200, "Morning run", LocalDateTime.of(2024, 4, 20, 8, 0), user));
        userTrainings.add(new Training("Swimming", 45, 300, "Evening swimming", LocalDateTime.of(2024, 4, 20, 18, 0), user));

        when(userService.getUser()).thenReturn(user);
        when(trainingDao.getUserTrainings(user)).thenReturn(userTrainings);

        double result = trainingService.getAverageCaloriesPerMinute();

        verify(auditService, times(1)).logStatisticsViewed("test_user");
    }

    @Test
    void printUserTrainings() {
        User user = new User("test_user", "password");
        List<Training> userTrainings = new ArrayList<>();
        userTrainings.add(new Training("Running", 30, 200, "Morning run", LocalDateTime.of(2024, 4, 20, 8, 0), user));

        when(trainingDao.getUserTrainings(user)).thenReturn(userTrainings);

        trainingService.printUserTrainings(user);

        verify(trainingDao, times(1)).getUserTrainings(user);
    }
}
