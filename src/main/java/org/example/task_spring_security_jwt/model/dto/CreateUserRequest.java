package org.example.task_spring_security_jwt.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс - запрос на регистрацию пользователя
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String userName;
    private String email;
    private String password;
}
