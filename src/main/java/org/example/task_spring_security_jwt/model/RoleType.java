package org.example.task_spring_security_jwt.model;

import lombok.Getter;

/**
 * Класс для хранения ролей
 */

@Getter
public enum RoleType {
    USER("USER"), ADMIN("ADMIN");

    private final String role;

    private RoleType(String role) {
        this.role = role;
    }
}
