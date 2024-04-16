package org.example.repositories;

import org.example.model.Training;
import org.example.model.User;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс TrainingRepository отвечает за хранение и управление данными тренировок.
 */
public class TrainingRepository {

    private final Map<User, List<Training>> trainings;

    public TrainingRepository() {
        this.trainings = new HashMap<>();
    }

    /**
     * Добавляет новую тренировку в репозиторий.
     * @param training Новая тренировка для добавления
     */
    public void addTraining(Training training) {
        trainings.computeIfAbsent(training.getUser(), k -> new ArrayList<>()).add(training);
    }


    /**
     * Получает все тренировки пользователя по его имени.
     * @param user Имя пользователя
     * @return Список тренировок пользователя.
     */
    public List<Training> getUserTrainings(User user) {
        return trainings.getOrDefault(user, new ArrayList<>());
    }


    /**
     * Получает список всех тренировк пользователей
     * @return Список тренировок пользователей
     */
    public List<Training> getAllTrainings() {
        return trainings.values().stream().flatMap(Collection::stream).toList();
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
    public void editTraining(User user, int trainingId, String type, int durationMinutes, int burnedCalories, String additionalInformation, LocalDateTime dateTime) {
        Training training = trainings.get(user).get(trainingId);
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
    public void deleteTraining(User user, int trainingId) {
        List<Training> trainings = this.trainings.get(user);
        trainings.remove(trainingId);
    }
}
