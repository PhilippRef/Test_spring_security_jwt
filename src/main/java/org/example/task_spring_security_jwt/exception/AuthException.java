package org.example.task_spring_security_jwt.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс для обработки исключений
 */

@Slf4j
public class AuthException extends RuntimeException {
    public AuthException() {
    }

    public AuthException(String message) {
        super(message);
        log.error(message);
    }
}
