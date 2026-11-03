package ru.skypro.homework;

import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.time.Instant;

/**
 * Готовые сущности для тестов.
 */
public final class TestData {

    public static final String USER_EMAIL = "user@gmail.com";
    public static final String USER_PASSWORD = "password";
    public static final Instant CREATED_AT = Instant.ofEpochMilli(1_700_000_000_000L);

    private TestData() {
    }

    public static UserEntity user() {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setPhone("+7 (999) 123-45-67");
        user.setRole(Role.USER);
        user.setImage("avatar.png");
        return user;
    }

    public static AdEntity ad(UserEntity author) {
        AdEntity ad = new AdEntity();
        ad.setId(10);
        ad.setTitle("Bicycle");
        ad.setPrice(5000);
        ad.setDescription("Good old bicycle");
        ad.setImage("ad.png");
        ad.setAuthor(author);
        return ad;
    }

    public static CommentEntity comment(AdEntity ad, UserEntity author) {
        CommentEntity comment = new CommentEntity();
        comment.setId(100);
        comment.setText("Is it still available?");
        comment.setCreatedAt(CREATED_AT);
        comment.setAd(ad);
        comment.setAuthor(author);
        return comment;
    }
}
