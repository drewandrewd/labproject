package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс User представляет собой сущность пользователя
 */
@Data
@NoArgsConstructor
public class User {

    private String name;
    private String password;
    private boolean isAdmin;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.isAdmin = false;
    }
}
