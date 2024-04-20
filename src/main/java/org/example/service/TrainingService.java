package org.example.service;

import lombok.Data;
import lombok.Setter;
import org.example.dao.AuditDao;
import org.example.dao.TrainingDao;
import org.example.model.Training;
import org.example.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * Класс TrainingService управляет операциями с тренировками пользователей.
 */
@Data
@Setter
public class TrainingService {

    private TrainingDao trainingDao;
    private UserService userService;
    private AuditService auditService;

    /**
     * Конструктор класса TrainingService.
     *
     * @param userService сервис пользователя
     */
    public TrainingService(UserService userService) {
        this.trainingDao = new TrainingDao();
        this.userService = userService;
        this.auditService = new AuditService();
    }

    /**
     * Добавляет новую тренировку с указанными параметрами.
     *
     * @param trainingType Тип тренировки
     * @param durationMinutes Продолжительность тренировки в минутах
     * @param burnedCalories  Потраченные калории
     * @param additionalInformation Дополнительная информация о тренировке
     * @param date Дата и время проведения тренировки
     */
    public void addTraining(String trainingType, int durationMinutes, int burnedCalories, String additionalInformation, String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
        Training training = new Training(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime, userService.getUser());
        trainingDao.addTraining(training);
        System.out.printf("Тренировка %s добавлена\n", trainingType);
        auditService.logTrainingAdded(userService.getUser().getName(), trainingType);
    }

    /**
     * Просмотр тренировок текущего пользователя.
     *
     * @param user пользователь
     */
    public void viewTrainings(User user) {
        List<Training> userTrainings = trainingDao.getUserTrainings(user);
        if (!userTrainings.isEmpty()) {
            userTrainings.sort(Comparator.comparing(Training::getDateTime));
            userTrainings.forEach(System.out::println);
        } else {
            System.out.println("Тренировки не добавлены");
        }
    }

    /**
     * Редактирует тренировку.
     *
     * @param trainingId id тренировки
     * @param type Тип тренировки
     * @param durationMinutes Продолжительность тренировки в минутах
     * @param burnedCalories Потраченные калории
     * @param additionalInformation Дополнительная информация о тренировке
     * @param date Дата и время проведения тренировки
     */
    public void editTraining(long trainingId, String type, int durationMinutes, int burnedCalories, String additionalInformation, String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
        trainingDao.editTraining(trainingId, type, durationMinutes, burnedCalories, additionalInformation, dateTime);
        System.out.println("Тренировка пользователя отредактирована.");
        auditService.logTrainingEdited(userService.getUser().getName(), type);
    }

    /**
     * Удаляет тренировку по id.
     *
     * @param trainingId id тренировки
     */
    public void deleteTraining(long trainingId) {
        trainingDao.deleteTraining(trainingId);
        System.out.println("Тренировка удалена.");
    }

    /**
     * Получение всех тренировок в виде списка.
     *
     * @param user пользователь
     * @return возвращает список тренировок
     */
    public List<Training> getUserTrainings(User user) {
        return trainingDao.getUserTrainings(user);
    }

    /**
     * Получение среднего количества калорий за минуту тренировки.
     *
     * @return возвращает среднее количество калорий за минуту тренировки
     */
    public double getAverageCaloriesPerMinute() {
        List<Training> userTrainings = trainingDao.getUserTrainings(userService.getUser());
        if (userTrainings.isEmpty()) {
            return 0;
        }
        int totalCalories = userTrainings.stream().mapToInt(Training::getBurnedCalories).sum();
        int totalDuration = userTrainings.stream().mapToInt(Training::getDurationMinutes).sum();
        auditService.logStatisticsViewed(userService.getUser().getName());
        return (double) totalCalories / totalDuration;
    }

    /**
     * Печать в консоли тренировок пользователя.
     *
     * @param user пользователь
     */
    public void printUserTrainings(User user) {
        List<Training> userTrainings = getUserTrainings(user);
        for (Training userTraining : userTrainings) {
            System.out.println(user.getId() + ". " + userTraining);
        }
    }
}
