package org.example.task_spring_security_jwt.repository;

import org.example.task_spring_security_jwt.entity.UserDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * Репозиторий для работы с пользователями (БД)
 */

public interface UserRepository extends JpaRepository<UserDB, Integer> {
    @Query("SELECT u FROM UserDB u WHERE u.userName = ?1")
    UserDB findByUserName(String username);

    @Query("SELECT u FROM UserDB u WHERE u.userEmail = ?1")
    UserDB findByUserEmail(String email);
}
