package org.example.task_spring_security_jwt.advice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Класс для формирования сообщения об ошибке
 */

@Setter
@Getter
@ToString
public class ErrorMessage {
    private String message;
    private Date timestamp;

    public ErrorMessage(String message, Date timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
