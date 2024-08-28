package org.example.task_spring_security_jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.task_spring_security_jwt.model.dto.*;
import org.example.task_spring_security_jwt.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер аутентификации
 */

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth controller")
public class AuthController {

    private final SecurityService securityService;

    /**
     * Контроллер для создания и сохранения нового пользователя
     * @param userDto
     * @return
     */
    @PostMapping("/registration")
    @Operation(summary = "Create new user", description = "Create new user")
    public ResponseEntity<UserDto> createNewUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(securityService.createNewUser(userDto));
    }

    /**
     * Контроллер для аутентификации пользователя
     * @param userAuthRequest
     * @return
     */
    @PostMapping("/")
    @Operation(summary = "Auth user", description = "Return token if auth success")
    public ResponseEntity<TokenResponse> authUser(@RequestBody UserAuthRequest userAuthRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(securityService.userAuth(userAuthRequest));
    }
}

