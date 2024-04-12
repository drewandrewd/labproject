package org.example.in;

import lombok.Data;
import lombok.Setter;
import org.example.model.Training;
import org.example.repositories.TrainingRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Класс TrainingController управляет операциями с тренировками пользователей
 */
@Data
@Setter
public class TrainingController {

    private final TrainingRepository trainingRepository;
    private final UserController userController;

    public TrainingController(UserController userController) {
        this.trainingRepository = new TrainingRepository();
        this.userController = userController;
    }



    /**
     * Добавляет новую тренировку с указанными параметрами
     * @param trainingType Тип тренировки
     * @param durationMinutes Продолжительность тренировки в минутах
     * @param burnedCalories Потраченные калории
     * @param additionalInformation Дополнительная информация о тренировке
     * @param dateTime Дата и время проведения тренировки
     */
    public void addTraining(String trainingType, int durationMinutes, int burnedCalories, String additionalInformation, LocalDateTime dateTime) {
        Training training = new Training(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime, userController.getUser());
        trainingRepository.addTraining(training);
        System.out.printf("Тренировка %s добавлена\n", trainingType);
        userController.getAuditRepository().logTrainingAdded(userController.getUser().getName(), trainingType);
    }

    /**
     * Просмотр тренировок текущего пользователя
     */
    public void viewTrainings() {
        List<Training> userTrainings = userController.isAdmin() ? trainingRepository.getAllTrainings() : trainingRepository.getUserTrainings(userController.getUser());
        if (!userTrainings.isEmpty()) {
            userTrainings.sort(Comparator.comparing(Training::getDateTime));
            userTrainings.forEach(System.out::println);
        } else {
            System.out.println("Тренировки не добавлены");
        }

    }

    /**
     * Редактирует тренировку
     * @param trainingId id тренировки.
     * @param type Тип тренировки.
     * @param durationMinutes Продолжительность тренировки в минутах.
     * @param burnedCalories Потраченные калории.
     * @param additionalInformation Дополнительная информация о тренировке.
     * @param dateTime Дата и время проведения тренировки.
     */
    public void editTraining(int trainingId, String type, int durationMinutes, int burnedCalories, String additionalInformation, LocalDateTime dateTime) {
        if (userController.isAdmin() || trainingRepository.getTrainingById(trainingId).getUser().equals(userController.getUser())) {
            trainingRepository.editTraining(trainingId, type, durationMinutes, burnedCalories, additionalInformation, dateTime);
            System.out.println("Тренировка отредактирована.");
            userController.getAuditRepository().logTrainingEdited(userController.getUser().getName(), type);
        } else {
            System.out.println("Нет доступа для редактирования");
        }
    }

    /**
     * Удаляет тренировку по id
     * @param trainingId id тренировки.
     */
    public void deleteTraining(int trainingId) {
        if (userController.isAdmin() || trainingRepository.getTrainingById(trainingId).getUser().equals(userController.getUser())) {
            userController.getAuditRepository().logTrainingAdded(userController.getUser().getName(), trainingRepository.getTrainingById(trainingId).getType());
            trainingRepository.deleteTraining(trainingId);
            System.out.println("Тренировка удалена.");
        } else {
            System.out.println("Нет доступа для удаления.");
        }
    }

    /**
     * Получение всех тренировок в виде списка
     * @return возврвщвет список тренировок.
     */
    public List<Training> getUserTrainings() {
        return trainingRepository.getUserTrainings(userController.getUser());
    }

    /**
     * Получение всех тренировок в виде списка
     * @return возврвщвет список тренировок.
     */
    public double getAverageCaloriesPerMinute() {
        List<Training> userTrainings = trainingRepository.getUserTrainings(userController.getUser());
        if (userTrainings.isEmpty()) {
            return 0;
        }
        int totalCalories = userTrainings.stream().mapToInt(Training::getBurnedCalories).sum();
        int totalDuration = userTrainings.stream().mapToInt(Training::getDurationMinutes).sum();
        userController.getAuditRepository().logStatisticsViewed(userController.getUser().getName());
        return (double) totalCalories / totalDuration;
    }
}
