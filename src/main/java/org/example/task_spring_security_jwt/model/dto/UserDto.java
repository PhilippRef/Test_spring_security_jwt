package org.example.task_spring_security_jwt.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.task_spring_security_jwt.model.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Класс DTO для работы с пользователями. Здесь инкапсулированы основные данные о пользователе
 */

@Getter
@Setter
@ToString
public class UserDto implements UserDetails {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private RoleType role;

    public UserDto() {
    }

    public UserDto(Integer id, String name, String email, String password, RoleType role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
