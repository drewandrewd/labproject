package org.example.service;

import lombok.Data;
import org.example.dao.AuditDao;
import org.example.dao.UserDao;
import org.example.model.User;

/**
 * Класс UserService управляет операциями с пользователями в системе.
 */
@Data
public class UserService {

    private static UserService instance;
    private UserDao userDao;
    private AuditService auditService;
    private User user;

    private UserService() {
        this.userDao = new UserDao();
        this.user = null;
        this.auditService = new AuditService();
    }

    /**
     * Получение единственного экземпляра класса UserService.
     *
     * @return возвращает экземпляр класса UserService
     */
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param userName Имя пользователя.
     * @param password Пароль пользователя.
     */
    public void register(String userName, String password) {
        User user = new User(userName, password);
        userDao.register(user);
        auditService.logAuthentication(userName, true);
    }

    /**
     * Авторизация пользователя.
     *
     * @param userName Имя пользователя.
     * @param password Пароль пользователя.
     */
    public void login(String userName, String password) {
        User user = userDao.login(userName, password);
        if (user != null) {
            setUser(user);
            System.out.println("Вход выполнен успешно");
            auditService.logAuthentication(userName, true);
        } else {
            System.out.println("Неправильный логин или пароль");
            auditService.logAuthentication(userName, false);
        }
    }

    /**
     * Выход из системы.
     */
    public void logout() {
        this.user = null;
        System.out.println("Выход из системы");
    }

    /**
     * Проверка, является ли текущий пользователь администратором.
     *
     * @return возвращает true, если пользователь является администратором, иначе false
     */
    public boolean isAdmin() {
        return this.user != null && this.user.isAdmin();
    }

    /**
     * Проверка, авторизован ли текущий пользователь.
     *
     * @return возвращает true, если пользователь авторизован, иначе false
     */
    public boolean isLoggedIn() {
        return this.user != null;
    }

    /**
     * Установка текущего пользователя.
     *
     * @param user пользователь
     */
    public void setCurrentUser(User user) {
        this.user = user;
    }

    /**
     * Печать всех пользователей в консоль.
     */
    public void printUsers() {
        userDao.getAllUsers().forEach(System.out::println);
    }

    /**
     * Получение пользователя по его имени пользователя (username).
     *
     * @param username имя пользователя
     * @return возвращает объект пользователя
     */
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
}
