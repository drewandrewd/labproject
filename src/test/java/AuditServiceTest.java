import org.example.dao.AuditDao;
import org.example.model.Audit;
import org.example.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

public class AuditServiceTest {

    @Mock
    private AuditDao auditDao;

    private AuditService auditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        auditService = new AuditService(auditDao);
    }

    @Test
    void logAuthentication() {
        String username = "test_user";
        boolean success = true;
        String expectedAction = "Авторизация - Логин: " + username + ", Success: " + success;
        LocalDateTime timestamp = LocalDateTime.of(2024, 4, 20, 8, 0);
        Audit audit = new Audit(username, expectedAction, timestamp);

        auditService.logAuthentication(username, success, timestamp);

        verify(auditDao).logEvent(audit);
    }

    @Test
    void logTrainingAdded() {
        String username = "test_user";
        String trainingType = "Running";
        String expectedAction = "Тренировка добавлена - Логин: " + username + ", Тип: " + trainingType;
        LocalDateTime timestamp = LocalDateTime.of(2024, 4, 20, 8, 0);
        Audit audit = new Audit(username, expectedAction, timestamp);

        auditService.logTrainingAdded(username, trainingType, timestamp);

        verify(auditDao).logEvent(audit);
    }

    @Test
    void logTrainingEdited() {
        String username = "test_user";
        String trainingType = "Running";
        String expectedAction = "Тренировка отредактирована - Логин: " + username + ", Тип: " + trainingType;
        LocalDateTime timestamp = LocalDateTime.of(2024, 4, 20, 8, 0);
        Audit audit = new Audit(username, expectedAction, timestamp);

        auditService.logTrainingEdited(username, trainingType, timestamp);

        verify(auditDao).logEvent(audit);
    }

    @Test
    void logTrainingDeleted() {
        String username = "test_user";
        String trainingType = "Running";
        String expectedAction = "Тренировка удалена - Логин: " + username + ", Тип: " + trainingType;
        LocalDateTime timestamp = LocalDateTime.of(2024, 4, 20, 8, 0);
        Audit audit = new Audit(username, expectedAction, timestamp);

        auditService.logTrainingDeleted(username, trainingType, timestamp);

        verify(auditDao).logEvent(audit);
    }

    @Test
    void logStatisticsViewed() {
        String username = "test_user";
        String expectedAction = "Просмотр статистики - Логин: " + username;
        LocalDateTime timestamp = LocalDateTime.of(2024, 4, 20, 8, 0);
        Audit audit = new Audit(username, expectedAction, timestamp);

        auditService.logStatisticsViewed(username, timestamp);

        verify(auditDao).logEvent(audit);
    }
}
