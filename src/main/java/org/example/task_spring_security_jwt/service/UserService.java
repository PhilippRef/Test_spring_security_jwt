package org.example.task_spring_security_jwt.service;

import lombok.RequiredArgsConstructor;
import org.example.task_spring_security_jwt.entity.UserDB;
import org.example.task_spring_security_jwt.exception.AuthException;
import org.example.task_spring_security_jwt.model.RoleType;
import org.example.task_spring_security_jwt.model.dto.UserDto;
import org.example.task_spring_security_jwt.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный класс для работы с пользователями
 */

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Метод для выгрузки данных пользователя по имени
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = findUserByName(username);

        return new User(
                userDto.getName(),
                userDto.getPassword(),
                userDto.getRole().equals(RoleType.ADMIN) ?
                        List.of(new SimpleGrantedAuthority(RoleType.ADMIN.name())) :
                        List.of(new SimpleGrantedAuthority(RoleType.USER.name())
                        ));
    }

    /**
     * Метод для создания и сохранения нового пользователя
     *
     * @param userDto
     * @return
     */
    public UserDto createUser(UserDto userDto) {

        Optional<UserDB> userDBName = Optional.ofNullable(userRepository.findByUserName(userDto.getName()));
        Optional<UserDB> userDBEmail = Optional.ofNullable(userRepository.findByUserEmail(userDto.getEmail()));

        if (userDBName.isPresent()) {
            throw new AuthException("Пользователь с именем " + userDto.getName() + " уже существует.");
        }
        if (userDBEmail.isPresent()) {
            throw new AuthException("Пользователь с почтой " + userDto.getEmail() + " уже существует.");
        }

        return saveNewUser(userDto);
    }

    /**
     * Метод для получения пользователя по имени
     *
     * @param userName - имя пользователя
     * @return UserDto
     */
    public UserDto findUserByName(String userName) {
        Optional<UserDB> userDB = Optional.ofNullable(userRepository.findByUserName(userName));

        return userDB.map(UserService::mapToDto)
                .orElseThrow(() -> new AuthException("Пользователь с именем " + userName + " не найден."));
    }


    /**
     * Метод для сохранения нового пользователя
     *
     * @param userDto
     * @return userDto
     */
    private UserDto saveNewUser(UserDto userDto) {
        UserDB userDB = mapToEntity(userDto);

        userDB.setUserPassword(passwordEncoder.encode(userDB.getUserPassword()));

        UserDB savedUser = userRepository.save(userDB);

        return mapToDto(savedUser);
    }


    /**
     * Метод для перевода данных из Dto в Entity
     *
     * @param userDB
     * @return UserDto
     */
    public static UserDto mapToDto(UserDB userDB) {
        UserDto userDto = new UserDto();

        userDto.setId(userDB.getId());
        userDto.setEmail(userDB.getUserEmail());
        userDto.setName(userDB.getUserName());
        userDto.setRole(userDB.getUserRole());
        userDto.setPassword(userDB.getUserPassword());

        return userDto;
    }

    /**
     * Метод для перевода данных из Entity в Dto
     *
     * @param userDto
     * @return UserDb
     */
    public static UserDB mapToEntity(UserDto userDto) {
        UserDB userDB = new UserDB();

        userDB.setId(userDto.getId());
        userDB.setUserEmail(userDto.getEmail());
        userDB.setUserName(userDto.getName());
        userDB.setUserPassword(userDto.getPassword());
        userDB.setUserRole(userDto.getRole());

        return userDB;
    }
}
