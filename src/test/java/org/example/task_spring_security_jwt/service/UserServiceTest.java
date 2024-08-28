package org.example.task_spring_security_jwt.service;

import org.example.task_spring_security_jwt.entity.UserDB;
import org.example.task_spring_security_jwt.exception.AuthException;
import org.example.task_spring_security_jwt.model.RoleType;
import org.example.task_spring_security_jwt.model.dto.UserDto;
import org.example.task_spring_security_jwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository userRepository =
            Mockito.mock(UserRepository.class);

    private final PasswordEncoder passwordEncoder =
            Mockito.mock(PasswordEncoder.class);

    private UserService userService;
    private UserDto userDto;
    private UserDB userDB;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
        addDataToUserDto();
    }

    @Test
    @DisplayName("test loadUserByUsername and return UserDetails when user exists")
    public void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        UserDB userDB = UserService.mapToEntity(userDto);

        when(userRepository.findByUserName(userDto.getName())).thenReturn(userDB);

        UserDetails userDetails = userService.loadUserByUsername(userDto.getName());

        assertNotNull(userDetails);
        assertEquals(userDto.getName(), userDetails.getUsername());
        assertEquals(userDto.getPassword(), userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(RoleType.USER.name())));

        verify(userRepository).findByUserName(userDto.getName());
        verify(userRepository, Mockito.times(1)).findByUserName(userDto.getName());
    }

    @Test
    @DisplayName("test loadUserByUsername and throw AuthException when user does not exist")
    public void loadUserByUsername_ShouldThrowAuthException_WhenUserDoesNotExist() {
        String username = "nonExistentUser";

        when(userRepository.findByUserName(username)).thenReturn(null);

        AuthException exception = assertThrows(AuthException.class, () -> userService.loadUserByUsername(username));

        assertEquals("Пользователь с именем " + username + " не найден.", exception.getMessage());

        verify(userRepository).findByUserName(username);
        verify(userRepository, Mockito.times(1)).findByUserName(username);
    }

    @Test
    @DisplayName("test createUser and return UserDto when user does not exist")
    public void createUser_ShouldSaveNewUser_WhenUserDoesNotExist() {
        String encodedPassword = "encodedPassword";

        when(userRepository.save(any(UserDB.class))).thenReturn(userDB);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn(encodedPassword);

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(userDto.getName(), createdUser.getName());
        assertEquals(userDto.getEmail(), createdUser.getEmail());
        assertEquals(userDto.getRole(), createdUser.getRole());

        verify(userRepository).save(any(UserDB.class));
        verify(passwordEncoder).encode(userDto.getPassword());
        verify(passwordEncoder, Mockito.times(1)).encode(userDto.getPassword());
    }

    @Test
    @DisplayName("test createUser and throw AuthException when username already exists")
    public void createUser_ShouldThrowAuthException_WhenUsernameAlreadyExists() {
        UserDB existingUser = new UserDB();

        existingUser.setUserName(userDto.getName());

        when(userRepository.findByUserName(userDto.getName())).thenReturn(existingUser);

        AuthException exception = assertThrows(AuthException.class, () -> userService.createUser(userDto));

        assertEquals("Пользователь с именем " + userDto.getName() + " уже существует.", exception.getMessage());

        verify(userRepository).findByUserName(userDto.getName());
        verify(userRepository, Mockito.times(1)).findByUserName(userDto.getName());
    }

    @Test
    @DisplayName("test createUser and throw AuthException when email already exists")
    public void createUser_ShouldThrowAuthException_WhenEmailAlreadyExists() {
        UserDB existingUser = new UserDB();

        existingUser.setUserEmail(userDto.getEmail());

        when(userRepository.findByUserEmail(userDto.getEmail())).thenReturn(existingUser);

        AuthException exception = assertThrows(AuthException.class, () -> userService.createUser(userDto));

        assertEquals("Пользователь с почтой " + userDto.getEmail() + " уже существует.", exception.getMessage());

        verify(userRepository).findByUserEmail(userDto.getEmail());
        verify(userRepository, Mockito.times(1)).findByUserEmail(userDto.getEmail());
    }

    @Test
    @DisplayName("test findUserByName and return UserDto when user exists")
    public void findUserByName_ShouldReturnUserDto_WhenUserExists() {
        UserDB userDB = UserService.mapToEntity(userDto);

        when(userRepository.findByUserName(userDto.getName())).thenReturn(userDB);

        UserDto foundUser = userService.findUserByName(userDto.getName());

        assertNotNull(foundUser);
        assertEquals(userDto.getName(), foundUser.getName());

        verify(userRepository).findByUserName(userDto.getName());
        verify(userRepository, Mockito.times(1)).findByUserName(userDto.getName());
    }

    @Test
    @DisplayName("test findUserByName and throw AuthException when user does not exist")
    public void findUserByName_ShouldThrowAuthException_WhenUserDoesNotExist() {
        String username = "nonExistentUser";

        when(userRepository.findByUserName(username)).thenReturn(null);

        AuthException exception = assertThrows(AuthException.class, () -> userService.findUserByName(username));

        assertEquals("Пользователь с именем " + username + " не найден.", exception.getMessage());

        verify(userRepository).findByUserName(username);
        verify(userRepository, Mockito.times(1)).findByUserName(username);
    }

    private void addDataToUserDto() {
        userDto = new UserDto();

        userDto.setName("testUser");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");
        userDto.setRole(RoleType.USER);

        userDB = new UserDB();

        userDB.setUserEmail(userDto.getEmail());
        userDB.setUserName(userDto.getName());
        userDB.setUserPassword(userDto.getPassword());
        userDB.setUserRole(userDto.getRole());
    }
}