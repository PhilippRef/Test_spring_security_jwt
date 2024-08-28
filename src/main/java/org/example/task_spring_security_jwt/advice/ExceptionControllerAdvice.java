package org.example.task_spring_security_jwt.advice;

import org.example.task_spring_security_jwt.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

/**
 * Перехватчик исключений
 */

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handleAuthException(AuthException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage(
                        ex.getMessage(),
                        new Date()));
    }
}

