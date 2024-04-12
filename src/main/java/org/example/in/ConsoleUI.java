package org.example.in;

import org.example.model.Training;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Класс ConsoleUI предоставляет консольный интерфейс для взаимодействия
 */
public class ConsoleUI {
    private final UserController userController;
    private final TrainingController trainingController;
    private final Scanner scanner;

    public ConsoleUI() {
        this.userController = new UserController();
        this.trainingController = new TrainingController(userController);
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запускает консольный интерфейс приложения
     */
    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    addTraining();
                    break;
                case "4":
                    viewTrainings();
                    break;
                case "5":
                    editTraining();
                    break;
                case "6":
                    viewStatistics();
                    break;
                case "7":
                    deleteTraining();
                    break;
                case "8":
                    logout();
                    break;
                case "9":
                    running = false;
                    break;
                default:
                    System.out.println("Неправильный ввод");
            }
        }
    }

    /**
     * Отображает главное меню приложения
     */
    private void printMainMenu() {
        System.out.println("Меню:");
        System.out.println("1. Регистрация");
        System.out.println("2. Вход в учетку");
        System.out.println("3. Добавить тренировку");
        System.out.println("4. Посмотреть свои тренировки");
        System.out.println("5. Редактировать тренировки");
        System.out.println("6. Посмотреть статистику по сожженным калориям");
        System.out.println("7. Удалить тренировку");
        System.out.println("8. Выход из учетки");
        System.out.println("9. Завершить работу приложения");
        System.out.print("Введите номер пункта меню: ");
    }

    /**
     * Метод для регистрации нового пользователя в системе. Запрашивает у пользователя имя и пароль
     */
    private void registerUser() {
        System.out.print("Имя: ");
        String username = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();
        userController.register(username, password);
    }

    /**
     * Метод для входа пользователя в систему. Запрашивает у пользователя имя и пароль
     */
    private void login() {
        System.out.print("Имя: ");
        String username = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();
        userController.login(username, password);
    }

    /**
     * Метод для добавления новой тренировки. Запрашивает у пользователя данные о тренировке (тип, длительность, количество потраченных калорий, дополнительная информация, дата и время)
     */
    private void addTraining() {
        if (userController.isLoggedIn()) {
            System.out.print("Тип тренировки: ");
            String type = scanner.nextLine();
            System.out.print("Длительность тренировки: ");
            int durationMinutes = scanner.nextInt();
            System.out.print("Колличество потраченных калорий: ");
            int caloriesBurned = scanner.nextInt();
            System.out.print("Дополнительная информация: ");
            scanner.nextLine();
            String additionalInformation = scanner.nextLine();
            System.out.print("Введите дату и время тренировки (HH:MM DD-MM-YYYY): ");
            LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
            trainingController.addTraining(type, durationMinutes, caloriesBurned, additionalInformation, dateTime);
        } else {
            System.out.println("Войдите в учетную запись");
        }
    }

    /**
     * Метод для просмотра всех тренировок пользователя.
     */
    private void viewTrainings() {
        trainingController.viewTrainings();
    }

    /**
     * Метод для редактирования тренировки. Показывает все предыдущие трениовки и позволяет выбрать тренировку для редактирования данных (тип, длительность, количество потраченных калорий, дополнительная информация, дата и время)
     */
    private void editTraining() {
        if (userController.isLoggedIn()) {
            System.out.println("Ваши тренировки:");
            List<Training> userTrainings = trainingController.getUserTrainings();
            for (int i = 0; i < userTrainings.size(); i++) {
                System.out.println(i + ". " + userTrainings.get(i));
            }
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
            LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
            trainingController.editTraining(id, type, durationMinutes, caloriesBurned, additionalInformation, dateTime);
        } else {
            System.out.println("Войдите в учетную запись");
        }
    }

    /**
     * Метод для удаления тренировки
     */
    private void deleteTraining() {
        if (userController.isLoggedIn()) {
            System.out.println("Ваши тренировки:");
            List<Training> userTrainings = trainingController.getUserTrainings();
            for (int i = 0; i < userTrainings.size(); i++) {
                System.out.println(i + ". " + userTrainings.get(i));
            }
            System.out.print("Введите номер тренировки для удаления: ");
            int index = scanner.nextInt();
            scanner.nextLine();
            trainingController.deleteTraining(index);
        } else {
            System.out.println("Войдите в учетную запись");
        }
    }

    /**
     * Выходит из учетной записи пользователя.
     */
    private void logout() {
        userController.logout();
    }

    /**
     * Отображает статистику по тренировкам текущего пользователя.
     */
    private void viewStatistics() {
        if (userController.isLoggedIn()) {
            double averageCaloriesPerMinute = trainingController.getAverageCaloriesPerMinute();
            System.out.println("Среднее число калорий сожженых в минуту: " + averageCaloriesPerMinute);
        } else {
            System.out.println("Войдите в учетную запись");
        }
    }
}

