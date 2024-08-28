package org.example.task_spring_security_jwt.model.dto;

import lombok.*;

/**
 * Класс для авторизации пользователя
 */

@Setter
@Getter
@ToString
public class UserAuthRequest {
    private String name;
    private String password;

    public UserAuthRequest() {
    }

    public UserAuthRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
