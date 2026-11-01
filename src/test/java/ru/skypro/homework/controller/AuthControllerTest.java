package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends IntegrationTest {

    private static final String REGISTER_JSON = "{\"username\":\"new@gmail.com\",\"password\":\"password\","
            + "\"firstName\":\"Anna\",\"lastName\":\"Petrova\",\"phone\":\"+79991234567\",\"role\":\"USER\"}";

    @Test
    void registerSavesUserWithHashedPassword() throws Exception {
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(REGISTER_JSON))
                .andExpect(status().isCreated());

        UserEntity user = userRepository.findByEmail("new@gmail.com").orElseThrow();
        assertThat(user.getPassword()).isNotEqualTo(PASSWORD);
        assertThat(passwordEncoder.matches(PASSWORD, user.getPassword())).isTrue();
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void registerExistingUserReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(REGISTER_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(REGISTER_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginWithCorrectPasswordReturnsOk() throws Exception {
        saveUser(USER_EMAIL, Role.USER);

        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user@gmail.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void loginWithWrongPasswordReturnsUnauthorized() throws Exception {
        saveUser(USER_EMAIL, Role.USER);

        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user@gmail.com\",\"password\":\"wrongpass\"}"))
                .andExpect(status().isUnauthorized());
    }
}
