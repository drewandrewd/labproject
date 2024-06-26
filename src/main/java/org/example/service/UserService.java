package org.example.service;

import lombok.Data;
import org.example.model.User;
import org.example.repositories.AuditRepository;
import org.example.repositories.UserRepository;

/**
 * Класс UserController управляет операциями с пользователями в системе.
 */
@Data
public class UserService {

    private final UserRepository userRepository;
    private final AuditRepository auditRepository;
    private User user;

    public UserService() {
        this.userRepository = new UserRepository();
        this.user = null;
        this.auditRepository = new AuditRepository();
    }

    /**
     * Регистрация пользователя
     * @param userName Имя пользователя.
     * @param password Пароль пользователя.
     */
    public void register(String userName, String password) {
        User user = new User(userName, password);
        userRepository.register(user);
    }

    /**
     * Авторизация пользователя
     * @param userName Имя пользователя.
     * @param password Пароль пользователя.
     */
    public void login(String userName, String password) {
        User user = userRepository.login(userName, password);
        if (user != null) {
            this.user = user;
            System.out.println("Вход выполнен успешно");
            auditRepository.logAuthentication(userName, true);
        } else {
            System.out.println("Неправильный логин или пароль");
            auditRepository.logAuthentication(userName, false);
        }
    }

    /**
     * Выход из системы
     */
    public void logout() {
        this.user = null;
        System.out.println("Выход из системы");
    }

    /**
     * Проверка на админа
     */
    public boolean isAdmin() {
        return this.user != null && this.user.isAdmin();
    }

    /**
     * Проверка на авторизацию
     */
    public boolean isLoggedIn() {
        return this.user != null;
    }

    /**
     * Присвоение пользователя контроллеру
     * @param user пользователь
     */
    public void setCurrentUser(User user) {
        this.user = user;
    }

    /**
     * Печать всех рользователей в консоль
     */
    public void printUsers() {
        userRepository.getAllUsers().keySet().forEach(System.out::println);
    }

    /**
     * Получение пользователя по username
     * @param username имя пользователя
     */
    public User getUserByUsername(String username) {
        return userRepository.getAllUsers().values().stream()
                .filter(user -> user.getName().equals(username))
                .findFirst()
                .orElse(null);
    }
}
