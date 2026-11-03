package ru.skypro.homework.controller;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.Instant;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

/**
 * Основа интеграционных тестов: приложение целиком на базе H2, данные очищаются после каждого теста.
 */
@SpringBootTest
@AutoConfigureMockMvc
abstract class IntegrationTest {

    protected static final String PASSWORD = "password";
    protected static final String USER_EMAIL = "user@gmail.com";
    protected static final String OTHER_EMAIL = "other@gmail.com";
    protected static final String ADMIN_EMAIL = "admin@gmail.com";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AdRepository adRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @AfterEach
    void cleanDatabase() {
        commentRepository.deleteAll();
        adRepository.deleteAll();
        userRepository.deleteAll();
    }

    protected UserEntity saveUser(String email, Role role) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setPhone("+7 (999) 123-45-67");
        user.setRole(role);
        return userRepository.save(user);
    }

    protected AdEntity saveAd(UserEntity author) {
        AdEntity ad = new AdEntity();
        ad.setTitle("Bicycle");
        ad.setPrice(5000);
        ad.setDescription("Good old bicycle");
        ad.setAuthor(author);
        return adRepository.save(ad);
    }

    protected CommentEntity saveComment(AdEntity ad, UserEntity author) {
        CommentEntity comment = new CommentEntity();
        comment.setText("Is it still available?");
        comment.setCreatedAt(Instant.now());
        comment.setAd(ad);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }

    protected RequestPostProcessor as(String email) {
        return httpBasic(email, PASSWORD);
    }
}
