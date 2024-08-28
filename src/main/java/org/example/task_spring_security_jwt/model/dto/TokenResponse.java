package org.example.task_spring_security_jwt.model.dto;

import lombok.*;

/**
 * Класс для ответа на токен
 */

@Getter
@Setter
@ToString
public class TokenResponse {
    private String token;

    public TokenResponse() {

    }

    public TokenResponse(String token) {
        this.token = token;
    }
}
