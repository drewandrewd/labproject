package org.example.in;

import org.example.model.User;
import org.example.service.TrainingService;
import org.example.service.UserService;

import java.util.Scanner;

public class AdminConsole {
    private final UserService userService;
    private final TrainingService trainingService;
    private final Scanner scanner;

    public AdminConsole(UserService userService, TrainingService trainingService, Scanner scanner) {
        this.userService = userService;
        this.trainingService = trainingService;
        this.scanner = scanner;
    }

    /**
     * Запускает консольный интерфейс меню администратора
     */
    public void start() {
        System.out.println("Все пользователи:");
        userService.printUsers();
        System.out.print("Выберите пользователя: ");
        String username = scanner.nextLine();
        User user = userService.getUserByUsername(username);
        if (user != null) {
            System.out.println("Выбранный пользователь: " + username);
            System.out.println("Тренировки выбранного пользователя:");
            trainingService.printUserTrainings(user);
            System.out.println("Выберите действие:");
            System.out.println("1. Редактировать тренировку");
            System.out.println("2. Удалить тренировку");
            System.out.print("Введите номер действия: ");
            int action = scanner.nextInt();
            scanner.nextLine();
            switch (action) {
                case 1:
                    editTraining(user);
                    break;
                case 2:
                    deleteTraining(user);
                    break;
                default:
                    System.out.println("Неправильный ввод");
            }
        } else {
            System.out.println("Пользователь с таким именем не найден.");
        }
    }

    /**
     * Метод для редактирования тренировки пользователя. Показывает все предыдущие трениовки и позволяет выбрать тренировку для редактирования данных (тип, длительность, количество потраченных калорий, дополнительная информация, дата и время)
     * @param user пользователь
     */
    private void editTraining(User user) {
        System.out.println("Тренировки пользователя " + user.getName() + " :");
        trainingService.printUserTrainings(user);
        System.out.print("Введите номер тренировки для редактирования: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Новый Тип тренировки: ");
        String type = scanner.nextLine();
        System.out.print("Новый Длительность тренировки: ");
        int durationMinutes = scanner.nextInt();
        System.out.print("Новый Колличество потраченных калорий: ");
        int caloriesBurned = scanner.nextInt();
        System.out.print("Новый Дополнительная информация: ");
        scanner.nextLine();
        String additionalInformation = scanner.nextLine();
        System.out.print("Новая дата и время тренировки (HH:MM DD-MM-YYYY): ");
        String dateTime = scanner.nextLine();
        trainingService.editTraining(id, type, durationMinutes, caloriesBurned, additionalInformation, dateTime);
    }

    /**
     * Метод для удаления тренировки пользователя
     * @param user пользователь
     */
    private void deleteTraining(User user) {
        System.out.println("Тренировки пользователя " + user.getName() + " :");
        trainingService.printUserTrainings(user);
        System.out.print("Введите номер тренировки для удаления: ");
        int index = scanner.nextInt();
        scanner.nextLine();
        trainingService.deleteTraining(userService.getUser(), index);
    }
}
