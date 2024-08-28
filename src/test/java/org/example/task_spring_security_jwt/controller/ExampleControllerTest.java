package org.example.task_spring_security_jwt.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class ExampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("test all access")
    public void testAllAccess() throws Exception {
        mockMvc.perform(get("/api/example/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("Unsecured data"));
    }

    @Test
    @DisplayName("test user access with user role")
    @WithMockUser(roles = "USER")
    public void testAdminAccess_UserRole() throws Exception {
        mockMvc.perform(get("/api/example/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("test user access with unauthorized")
    public void testUserAccess_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/example/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("test admin access with unauthorized")
    public void testAdminAccess_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/example/admin"))
                .andExpect(status().isForbidden());
    }
}