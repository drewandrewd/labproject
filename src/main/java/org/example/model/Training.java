package org.example.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Класс Training представляет собой тренировку пользователя
 */
@Data
public class Training {

    private Long id;
    private String type;
    private int durationMinutes;
    private int burnedCalories;
    private String additionalInformation;
    private LocalDateTime dateTime;
    private User user;


    public Training(String type, int durationMinutes, int burnedCalories, String additionalInformation, LocalDateTime dateTime, User user) {
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.burnedCalories = burnedCalories;
        this.dateTime = dateTime;
        this.additionalInformation = additionalInformation;
        this.user = user;
    }

    public Training() {
    }
}
