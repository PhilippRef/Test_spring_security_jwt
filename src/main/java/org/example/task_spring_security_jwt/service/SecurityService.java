package org.example.task_spring_security_jwt.service;

import lombok.RequiredArgsConstructor;
import org.example.task_spring_security_jwt.exception.AuthException;
import org.example.task_spring_security_jwt.model.dto.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Сервисный класс с описанием логики работы с аутентификацией и созданием пользователя
 */

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    /**
     * Метод аутентификации пользователя
     * @param userAuthRequest
     * @return
     */
    public TokenResponse userAuth(UserAuthRequest userAuthRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userAuthRequest.getName(),
                    userAuthRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AuthException("Неправильный логин или пароль");
        }

        UserDetails userDetails = userService.loadUserByUsername(userAuthRequest.getName());

        UserDto userDto = userService.findUserByName(userDetails.getUsername());

        String token = tokenService.generateToken(userDto);

        return new TokenResponse(token);
    }

    /**
     * Метод создания нового пользователя
     * @param userDto
     * @return
     */
    public UserDto createNewUser(UserDto userDto) {
        return userService.createUser(userDto);
    }
}
