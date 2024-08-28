package org.example.task_spring_security_jwt.service;

import org.example.task_spring_security_jwt.exception.AuthException;
import org.example.task_spring_security_jwt.model.RoleType;
import org.example.task_spring_security_jwt.model.dto.TokenResponse;
import org.example.task_spring_security_jwt.model.dto.UserAuthRequest;
import org.example.task_spring_security_jwt.model.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    private SecurityService securityService;
    private UserDto userDto;

    private final UserService userService =
            Mockito.mock(UserService.class);
    private final TokenService tokenService =
            Mockito.mock(TokenService.class);
    private final AuthenticationManager authenticationManager =
            Mockito.mock(AuthenticationManager.class);
    private final UserDetails userDetails =
            Mockito.mock(UserDetails.class);

    @BeforeEach
    void setUp() {
        securityService = new SecurityService(userService, tokenService, authenticationManager);
        userDto = new UserDto();
    }

    @Test
    @DisplayName("test UserAuth and return tokenResponse when user is valid")
    void testUserAuth_ShouldReturnTokenResponse_WhenUserIsValid() {

        UserAuthRequest userAuthRequest = new UserAuthRequest("validUser", "validPassword");

        userDto.setName("validUser");

        when(userDetails.getUsername()).thenReturn("validUser");
        when(userService.loadUserByUsername("validUser")).thenReturn(userDetails);
        when(userService.findUserByName("validUser")).thenReturn(userDto);
        when(tokenService.generateToken(userDto)).thenReturn("token123");

        TokenResponse tokenResponse = securityService.userAuth(userAuthRequest);

        assertNotNull(tokenResponse);
        assertEquals("token123", tokenResponse.getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("test UserAuth and throw AuthException when user is invalid")
    void userAuth_ShouldThrowAuthException_WhenUserIsInvalid() {
        UserAuthRequest userAuthRequest = new UserAuthRequest("invalidUser", "invalidPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        AuthException exception = assertThrows(AuthException.class,
                () -> securityService.userAuth(userAuthRequest));

        assertEquals("Неправильный логин или пароль", exception.getMessage());
    }

    @Test
    @DisplayName("test create NewUser and return userDto")
    void createNewUser_ShouldReturnUserDto_WhenUserIsCreated() {
        userDto.setName("newUser");
        userDto.setEmail("newUser@newUser.com");
        userDto.setRole(RoleType.USER);
        userDto.setPassword("newUserPassword");

        when(userService.createUser(userDto)).thenReturn(userDto);

        UserDto createdUser = securityService.createNewUser(userDto);

        assertNotNull(createdUser);
        assertEquals("newUser", createdUser.getUsername());
        assertEquals("newUser@newUser.com", createdUser.getEmail());
        assertEquals(RoleType.USER, createdUser.getRole());
        assertEquals("newUserPassword", createdUser.getPassword());

        verify(userService, times(1)).createUser(userDto);
    }
}