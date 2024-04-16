package org.example.repositories;

import lombok.Data;
import org.example.model.User;

import java.util.*;

/**
 * Класс UserRepository отвечает за хранение и управление данными пользователей
 */
@Data
public class UserRepository {

    private final Map<String, User> allUsers;

    public UserRepository() {
        allUsers = new HashMap<>();
    }

    /**
     * Добавляет нового пользователя в репозиторий
     * @param user Новый пользователь
     */
    public void register(User user) {
        allUsers.put(user.getName(), user);
    }

    /**
     * Ищет пользователя в репозитории по имени и паролю, возвращает полность объект User
     * @param name Имя пользователя
     * @param password Пароль
     * @return Объект User
     */
    public User login(String name, String password) {
        Optional<User> userOptional = Optional.ofNullable(allUsers.get(name));
        return userOptional.filter(user -> Objects.equals(user.getPassword(), password))
                .orElse(null);
    }
}
