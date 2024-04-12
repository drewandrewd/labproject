package org.example.repositories;

import org.example.model.Training;
import org.example.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс TrainingRepository отвечает за хранение и управление данными тренировок.
 */
public class TrainingRepository {

    private final List<Training> trainings;

    public TrainingRepository() {
        this.trainings = new ArrayList<>();
    }

    /**
     * Добавляет новую тренировку в репозиторий.
     * @param training Новая тренировка для добавления
     */
    public void addTraining(Training training) {
        trainings.add(training);
    }


    /**
     * Получает все тренировки пользователя по его имени.
     * @param user Имя пользователя
     * @return Список тренировок пользователя.
     */
    public List<Training> getUserTrainings(User user) {
        List<Training> userTrainings = new ArrayList<>();
        trainings.forEach(
                training -> {
                    if (training.getUser().equals(user)) {
                        userTrainings.add(training);
                    }
                }
        );
        return userTrainings;
    }


    /**
     * Получает список всех тренировк пользователей
     * @return Список тренировок пользователей
     */
    public List<Training> getAllTrainings() {
        return trainings;
    }

    /**
     * Возвращает тренировку по id
     * @param id
     * @return тренировка
     */
    public Training getTrainingById(int id) {
        return trainings.get(id);
    }

    /**
     * Изменяет данные тренировки
     * @param trainingId id тренировки.
     * @param type Тип тренировки.
     * @param durationMinutes Продолжительность тренировки в минутах.
     * @param burnedCalories Потраченные калории.
     * @param additionalInformation Дополнительная информация о тренировке.
     * @param dateTime Дата и время проведения тренировки.
     */
    public void editTraining(int trainingId, String type, int durationMinutes, int burnedCalories, String additionalInformation, LocalDateTime dateTime) {
        Training training = trainings.get(trainingId);
        training.setType(type);
        training.setDurationMinutes(durationMinutes);
        training.setBurnedCalories(burnedCalories);
        training.setAdditionalInformation(additionalInformation);
        training.setDateTime(dateTime);
    }

    /**
     * Удаляет тренировку из репозитория по id
     * @param trainingId id тренировки
     */
    public void deleteTraining(int trainingId) {
        trainings.remove(trainingId);
    }
}
