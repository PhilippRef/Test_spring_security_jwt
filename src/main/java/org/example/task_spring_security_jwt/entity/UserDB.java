package org.example.task_spring_security_jwt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.task_spring_security_jwt.model.RoleType;

/**
 * Класс для хранения данных о пользователях
 */

@Entity
@Builder
@Getter
@Setter
@Table(name = "users")
public class UserDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, columnDefinition = "enum('USER', 'ADMIN')")
    private RoleType userRole;

    public UserDB() {

    }

    public UserDB(Integer id, String userName, String userPassword, String userEmail, RoleType userRole) {
        this.id = id;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userRole = userRole;
    }
}
