package org.example.task_spring_security_jwt.controller;

import org.example.task_spring_security_jwt.exception.AuthException;
import org.example.task_spring_security_jwt.model.dto.TokenResponse;
import org.example.task_spring_security_jwt.model.dto.UserAuthRequest;
import org.example.task_spring_security_jwt.model.dto.UserDto;
import org.example.task_spring_security_jwt.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private final SecurityService securityService =
            Mockito.mock(SecurityService.class);

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(securityService);
    }

    @Test
    @DisplayName("test createNewUser")
    public void testCreateNewUser() {
        UserDto userDto = new UserDto();

        userDto.setName("username");
        userDto.setPassword("password");

        when(securityService.createNewUser(userDto)).thenReturn(userDto);

        ResponseEntity<?> response = authController.createNewUser(userDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDto, response.getBody());

        verify(securityService, Mockito.times(1)).createNewUser(userDto);
    }

    @Test
    @DisplayName("test authUser")
    void testAuthUser() {
        UserAuthRequest userAuthRequest = new UserAuthRequest("username", "password");
        TokenResponse tokenResponse = new TokenResponse("token");

        when(securityService.userAuth(any(UserAuthRequest.class))).thenReturn(tokenResponse);

        ResponseEntity<?> response = authController.authUser(userAuthRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tokenResponse, response.getBody());

        verify(securityService, Mockito.times(1)).userAuth(userAuthRequest);
    }

    @Test
    void testAuthUserWhenUserIsInvalid() {
        UserAuthRequest userAuthRequest = new UserAuthRequest("username", "password");

        when(securityService.userAuth(any(UserAuthRequest.class))).thenThrow(new AuthException("Invalid credentials"));

        assertThrows(AuthException.class, () -> authController.authUser(userAuthRequest));

        verify(securityService, Mockito.times(1)).userAuth(userAuthRequest);
    }
}