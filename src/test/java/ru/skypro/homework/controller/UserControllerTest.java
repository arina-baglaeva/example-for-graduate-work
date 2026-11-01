package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.skypro.homework.dto.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends IntegrationTest {

    @Test
    void getUserReturnsCurrentUser() throws Exception {
        saveUser(USER_EMAIL, Role.USER);

        mockMvc.perform(get("/users/me").with(as(USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(USER_EMAIL))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void getUserWithoutAuthReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateUserChangesProfile() throws Exception {
        saveUser(USER_EMAIL, Role.USER);

        mockMvc.perform(patch("/users/me").with(as(USER_EMAIL)).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Petr\",\"lastName\":\"Petrov\",\"phone\":\"+79990001122\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Petr"));

        assertThat(userRepository.findByEmail(USER_EMAIL).orElseThrow().getLastName()).isEqualTo("Petrov");
    }

    @Test
    void setPasswordWithCorrectCurrentPasswordSavesNewHash() throws Exception {
        saveUser(USER_EMAIL, Role.USER);

        mockMvc.perform(post("/users/set_password").with(as(USER_EMAIL)).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currentPassword\":\"password\",\"newPassword\":\"newpassword\"}"))
                .andExpect(status().isOk());

        String hash = userRepository.findByEmail(USER_EMAIL).orElseThrow().getPassword();
        assertThat(passwordEncoder.matches("newpassword", hash)).isTrue();
    }

    @Test
    void setPasswordWithWrongCurrentPasswordReturnsBadRequest() throws Exception {
        saveUser(USER_EMAIL, Role.USER);

        mockMvc.perform(post("/users/set_password").with(as(USER_EMAIL)).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currentPassword\":\"wrongpass\",\"newPassword\":\"newpassword\"}"))
                .andExpect(status().isBadRequest());
    }
}
