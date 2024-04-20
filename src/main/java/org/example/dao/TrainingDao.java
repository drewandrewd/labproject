package org.example.dao;

import org.example.config.DatabaseConfig;
import org.example.model.Training;
import org.example.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс TrainingDao отвечает за хранение и управление данными тренировок.
 */
public class TrainingDao {

    private static final String INSERT_TRAINING_SQL = "INSERT INTO trainings (type, duration_minutes, calories_burned, additional_information, datetime, user_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_TRAININGS_BY_USER_SQL = "SELECT * FROM trainings WHERE user_id = ?";
    private static final String SELECT_ALL_TRAININGS_SQL = "SELECT * FROM trainings";
    private static final String UPDATE_TRAINING_SQL = "UPDATE trainings SET type = ?, duration_minutes = ?, calories_burned = ?, additional_information = ?, datetime = ? WHERE id = ?";
    private static final String DELETE_TRAINING_SQL = "DELETE FROM trainings WHERE id = ?";

    /**
     * Добавляет новую тренировку.
     *
     * @param training тренировка для добавления
     */
    public void addTraining(Training training) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRAINING_SQL)) {
            preparedStatement.setString(1, training.getType());
            preparedStatement.setInt(2, training.getDurationMinutes());
            preparedStatement.setInt(3, training.getBurnedCalories());
            preparedStatement.setString(4, training.getAdditionalInformation());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(training.getDateTime()));
            preparedStatement.setLong(6, training.getUser().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает список всех тренировок пользователя.
     *
     * @param user пользователь, тренировки которого необходимо получить
     * @return список тренировок пользователя
     */
    public List<Training> getUserTrainings(User user) {
        List<Training> trainings = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRAININGS_BY_USER_SQL)) {
            preparedStatement.setLong(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                trainings.add(mapTraining(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainings;
    }

    /**
     * Возвращает список всех тренировок.
     *
     * @return список всех тренировок
     */
    public List<Training> getAllTrainings() {
        List<Training> trainings = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_TRAININGS_SQL)) {
            while (resultSet.next()) {
                trainings.add(mapTraining(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainings;
    }

    /**
     * Редактирует тренировку.
     *
     * @param trainingId id тренировки
     * @param type тип тренировки
     * @param durationMinutes продолжительность тренировки в минутах
     * @param burnedCalories потраченные калории
     * @param additionalInformation дополнительная информация о тренировке
     * @param dateTime дата и время проведения тренировки
     */
    public void editTraining(long trainingId, String type, int durationMinutes, int burnedCalories, String additionalInformation, LocalDateTime dateTime) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TRAINING_SQL)) {
            preparedStatement.setString(1, type);
            preparedStatement.setInt(2, durationMinutes);
            preparedStatement.setInt(3, burnedCalories);
            preparedStatement.setString(4, additionalInformation);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(dateTime));
            preparedStatement.setLong(6, trainingId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаляет тренировку по id.
     *
     * @param trainingId id тренировки
     */
    public void deleteTraining(long trainingId) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TRAINING_SQL)) {
            preparedStatement.setLong(1, trainingId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Training mapTraining(ResultSet resultSet) throws SQLException {
        Training training = new Training();
        training.setId(resultSet.getLong("id"));
        training.setType(resultSet.getString("type"));
        training.setDurationMinutes(resultSet.getInt("duration_minutes"));
        training.setBurnedCalories(resultSet.getInt("calories_burned"));
        training.setAdditionalInformation(resultSet.getString("additional_information"));
        training.setDateTime(resultSet.getTimestamp("datetime").toLocalDateTime());
        training.setUser(getUserFromResultSet(resultSet));
        return training;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("user_id"));
        return user;
    }
}

