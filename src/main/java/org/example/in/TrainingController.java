package org.example.in;

import lombok.Data;
import lombok.Setter;
import org.example.model.Training;
import org.example.model.User;
import org.example.repositories.TrainingRepository;
import org.example.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
     * @param date Дата и время проведения тренировки
     */
    public void addTraining(String trainingType, int durationMinutes, int burnedCalories, String additionalInformation, String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
        Training training = new Training(trainingType, durationMinutes, burnedCalories, additionalInformation, dateTime, userController.getUser());
        trainingRepository.addTraining(training);
        System.out.printf("Тренировка %s добавлена\n", trainingType);
        userController.getAuditRepository().logTrainingAdded(userController.getUser().getName(), trainingType);
    }

    /**
     * Просмотр тренировок текущего пользователя
     */
    public void viewTrainings(User user) {
        List<Training> userTrainings = trainingRepository.getUserTrainings(user);
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
     * @param date Дата и время проведения тренировки.
     */
    public void editTraining(int trainingId, String type, int durationMinutes, int burnedCalories, String additionalInformation, String date) {
            LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
            trainingRepository.editTraining(userController.getUser(), trainingId, type, durationMinutes, burnedCalories, additionalInformation, dateTime);
            System.out.println("Тренировка пользователя отредактирована.");
            userController.getAuditRepository().logTrainingEdited(userController.getUser().getName(), type);
    }

    /**
     * Удаляет тренировку по id
     * @param trainingId id тренировки.
     */
    public void deleteTraining(User user, int trainingId) {
            userController.getAuditRepository().logTrainingAdded(user.getName(), trainingRepository.getUserTrainings(user).get(trainingId).getType());
            trainingRepository.deleteTraining(user, trainingId);
            System.out.println("Тренировка удалена.");
    }

    /**
     * Получение всех тренировок в виде списка
     * @return возврвщвет список тренировок.
     */
    public List<Training> getUserTrainings(User user) {
        return trainingRepository.getUserTrainings(user);
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

    /**
     * Печать в консоли тренировок пользователя
     * @param user пользователь
     */
    public void printUserTrainings(User user) {
        List<Training> userTrainings = getUserTrainings(user);
        for (int i = 0; i < userTrainings.size(); i++) {
            System.out.println(i + ". " + userTrainings.get(i));
        }
    }
}
