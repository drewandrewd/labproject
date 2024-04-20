package org.example.service;

import lombok.Data;
import org.example.dao.AuditDao;
import org.example.model.Audit;

import java.time.LocalDateTime;

/**
 * Класс AuditService управляет операциями аудита в приложении.
 */
@Data
public class AuditService {

    private AuditDao auditDao;

    /**
     * Конструктор класса AuditService.
     */
    public AuditService() {
        this.auditDao = new AuditDao();
    }

    /**
     * Конструктор класса AuditService.
     *
     * @param auditDao объект класса AuditDao
     */
    public AuditService(AuditDao auditDao) {
        this.auditDao = auditDao;
    }

    /**
     * Регистрирует попытку авторизации пользователя.
     *
     * @param username имя пользователя
     * @param success  успешна ли попытка авторизации
     */
    public void logAuthentication(String username, boolean success) {
        String action = "Авторизация - Логин: " + username + ", Success: " + success;
        Audit audit = new Audit(username, action, LocalDateTime.now());
        auditDao.logEvent(audit);
    }

    /**
     * Регистрирует попытку добавления новой тренировки.
     *
     * @param username имя пользователя
     * @param trainingType тип тренировки
     */
    public void logTrainingAdded(String username, String trainingType) {
        String action = "Тренировка добавлена - Логин: " + username + ", Тип: " + trainingType;
        Audit audit = new Audit(username, action, LocalDateTime.now());
        auditDao.logEvent(audit);
    }

    /**
     * Регистрирует попытку редактирования новой тренировки.
     *
     * @param username имя пользователя
     * @param trainingType тип тренировки
     */
    public void logTrainingEdited(String username, String trainingType) {
        String action = "Тренировка отредактирована - Логин: " + username + ", Тип: " + trainingType;
        Audit audit = new Audit(username, action, LocalDateTime.now());
        auditDao.logEvent(audit);
    }

    /**
     * Регистрирует попытку удаления новой тренировки.
     *
     * @param username имя пользователя
     * @param trainingType тип тренировки
     */
    public void logTrainingDeleted(String username, String trainingType) {
        String action = "Тренировка удалена - Логин: " + username + ", Тип: " + trainingType;
        Audit audit = new Audit(username, action, LocalDateTime.now());
        auditDao.logEvent(audit);
    }

    /**
     * Регистрирует попытку просмотра статистики тренировок.
     *
     * @param username имя пользователя
     */
    public void logStatisticsViewed(String username) {
        String action = "Просмотр статистики - Логин: " + username;
        Audit audit = new Audit(username, action, LocalDateTime.now());
        auditDao.logEvent(audit);
    }
}
