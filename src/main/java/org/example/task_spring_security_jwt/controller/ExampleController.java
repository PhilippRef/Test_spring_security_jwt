package org.example.task_spring_security_jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/example")
@Tag(name = "ExampleController")
public class ExampleController {

    /**
     * Метод доступен не авторизованным пользователям
     *
     * @return String
     */
    @GetMapping("/all")
    @Operation(summary = "All access", description = "Return all unsecured data")
    public String allAccess() {
        return "Unsecured data";
    }

    /**
     * Метод доступен только авторизованным пользователям USER и ADMIN
     *
     * @return String
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "User access", description = "Return user data if authorized")
    public String userAccess() {
        return "User data";
    }

    /**
     * Метод доступен только авторизованным пользователям Admin
     *
     * @return String
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin access", description = "Return admin data if authorized")
    public String adminAccess() {
        return "Admin data";
    }
}
