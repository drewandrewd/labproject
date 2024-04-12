package org.example.repositories;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс AuditLogger отвечает за регистрацию действий пользователей
 */
@Getter
public class AuditRepository {

    private final List<String> auditLog;

    public AuditRepository() {
        this.auditLog = new ArrayList<>();
    }

    /**
     * Регистрирует попытку авторизации пользователя
     * @param username Имя пользователя
     * @param success успешна ли попытка авторизации
     */
    public void logAuthentication(String username, boolean success) {
        String message = "[" + LocalDateTime.now() + "] Авторизация - Логин: " + username + ", Success: " + success;
        auditLog.add(message);
    }

    /**
     * Регистрирует попытку добавления новой тренировки
     * @param username Имя пользователя
     * @param trainingType успешна ли попытка авторизации
     */
    public void logTrainingAdded(String username, String trainingType) {
        String message = "[" + LocalDateTime.now() + "] Тренировка добавлена - Логин: " + username + ", Тип: " + trainingType;
        auditLog.add(message);
    }

    /**
     * Регистрирует попытку редактирования новой тренировки
     * @param username Имя пользователя
     * @param trainingType успешна ли попытка авторизации
     */
    public void logTrainingEdited(String username, String trainingType) {
        String message = "[" + LocalDateTime.now() + "] Тренировка отредактирована - Логин: " + username + ", Тип: " + trainingType;
        auditLog.add(message);
    }

    /**
     * Регистрирует попытку удаления новой тренировки
     * @param username Имя пользователя
     * @param trainingType успешна ли попытка авторизации
     */
    public void logTrainingDeleted(String username, String trainingType) {
        String message = "[" + LocalDateTime.now() + "] Тренировка удалена - Логин: " + username + ", Тип: " + trainingType;
        auditLog.add(message);
    }

    /**
     * Регистрирует попытку просмотра статистики тренировок
     * @param username Имя пользователя
     */
    public void logStatisticsViewed(String username) {
        String message = "[" + LocalDateTime.now() + "] Просмотр статистики - Логин: " + username;
        auditLog.add(message);
    }

}
