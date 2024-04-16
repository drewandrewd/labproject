package org.example.in;

import org.example.service.TrainingService;
import org.example.service.UserService;

import java.util.Scanner;

/**
 * Класс ConsoleUI предоставляет консольный интерфейс для взаимодействия
 */
public class ConsoleUI {
    private final UserService userService;
    private final TrainingService trainingService;
    private final Scanner scanner;

    public ConsoleUI() {
        this.userService = new UserService();
        this.trainingService = new TrainingService(userService);
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
                case "10":
                    adminManagingTrainings();
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
        if (userService.isAdmin()) {
            System.out.println("10. Управление тренировками пользователей:");
        }
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
        userService.register(username, password);
    }

    /**
     * Метод для входа пользователя в систему. Запрашивает у пользователя имя и пароль
     */
    private void login() {
        System.out.print("Имя: ");
        String username = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();
        userService.login(username, password);
    }

    /**
     * Метод для добавления новой тренировки. Запрашивает у пользователя данные о тренировке (тип, длительность, количество потраченных калорий, дополнительная информация, дата и время)
     */
    private void addTraining() {
        if (userService.isLoggedIn()) {
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
            String dateTime = scanner.nextLine();
            trainingService.addTraining(type, durationMinutes, caloriesBurned, additionalInformation, dateTime);
        } else {
            System.out.println("Войдите в учетную запись");
        }
    }

    /**
     * Метод для просмотра всех тренировок пользователя.
     */
    private void viewTrainings() {
        trainingService.viewTrainings(userService.getUser());
    }

    /**
     * Метод для редактирования тренировки. Показывает все предыдущие трениовки и позволяет выбрать тренировку для редактирования данных (тип, длительность, количество потраченных калорий, дополнительная информация, дата и время)
     */
    private void editTraining() {
        if (userService.isLoggedIn()) {
            System.out.println("Ваши тренировки:");
            trainingService.printUserTrainings(userService.getUser());
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
        } else {
            System.out.println("Войдите в учетную запись");
        }
    }

    /**
     * Метод для удаления тренировки
     */
    private void deleteTraining() {
        if (userService.isLoggedIn()) {
            System.out.println("Ваши тренировки:");
            trainingService.printUserTrainings(userService.getUser());
            System.out.print("Введите номер тренировки для удаления: ");
            int index = scanner.nextInt();
            scanner.nextLine();
            trainingService.deleteTraining(userService.getUser(), index);
        } else {
            System.out.println("Войдите в учетную запись");
        }
    }

    /**
     * Выходит из учетной записи пользователя.
     */
    private void logout() {
        userService.logout();
    }

    /**
     * Отображает статистику по тренировкам текущего пользователя.
     */ 
    private void viewStatistics() {
        if (userService.isLoggedIn()) {
            double averageCaloriesPerMinute = trainingService.getAverageCaloriesPerMinute();
            System.out.println("Среднее число калорий сожженых в минуту: " + averageCaloriesPerMinute);
        } else {
            System.out.println("Войдите в учетную запись");
        }
    }

    /**
     * Вызывает консольное меню для админа
     */
    private void adminManagingTrainings() {
        AdminConsole adminConsole = new AdminConsole(userService, trainingService, scanner);
        adminConsole.start();
    }
}

