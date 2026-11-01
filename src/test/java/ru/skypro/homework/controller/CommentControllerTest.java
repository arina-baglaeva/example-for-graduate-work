package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends IntegrationTest {

    private static final String COMMENT_JSON = "{\"text\":\"Can I see it tomorrow?\"}";

    @Test
    void getCommentsReturnsCommentsOfAd() throws Exception {
        UserEntity user = saveUser(USER_EMAIL, Role.USER);
        AdEntity ad = saveAd(user);
        saveComment(ad, user);

        mockMvc.perform(get("/ads/" + ad.getId() + "/comments").with(as(USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].authorFirstName").value("Ivan"));
    }

    @Test
    void getCommentsOfMissingAdReturnsNotFound() throws Exception {
        saveUser(USER_EMAIL, Role.USER);

        mockMvc.perform(get("/ads/999/comments").with(as(USER_EMAIL)))
                .andExpect(status().isNotFound());
    }

    @Test
    void addCommentSavesCommentOfCurrentUser() throws Exception {
        AdEntity ad = saveAd(saveUser(USER_EMAIL, Role.USER));
        UserEntity other = saveUser(OTHER_EMAIL, Role.USER);

        mockMvc.perform(post("/ads/" + ad.getId() + "/comments").with(as(OTHER_EMAIL))
                        .contentType(MediaType.APPLICATION_JSON).content(COMMENT_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(other.getId()))
                .andExpect(jsonPath("$.createdAt").isNumber());
    }

    @Test
    void addTooShortCommentReturnsBadRequest() throws Exception {
        AdEntity ad = saveAd(saveUser(USER_EMAIL, Role.USER));

        mockMvc.perform(post("/ads/" + ad.getId() + "/comments").with(as(USER_EMAIL))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"short\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void userCannotUpdateSomeoneElsesComment() throws Exception {
        UserEntity user = saveUser(USER_EMAIL, Role.USER);
        CommentEntity comment = saveComment(saveAd(user), user);
        saveUser(OTHER_EMAIL, Role.USER);

        mockMvc.perform(patch(commentUrl(comment)).with(as(OTHER_EMAIL))
                        .contentType(MediaType.APPLICATION_JSON).content(COMMENT_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void authorCanUpdateOwnComment() throws Exception {
        UserEntity user = saveUser(USER_EMAIL, Role.USER);
        CommentEntity comment = saveComment(saveAd(user), user);

        mockMvc.perform(patch(commentUrl(comment)).with(as(USER_EMAIL))
                        .contentType(MediaType.APPLICATION_JSON).content(COMMENT_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Can I see it tomorrow?"));
    }

    @Test
    void adminCanDeleteSomeoneElsesComment() throws Exception {
        UserEntity user = saveUser(USER_EMAIL, Role.USER);
        CommentEntity comment = saveComment(saveAd(user), user);
        saveUser(ADMIN_EMAIL, Role.ADMIN);

        mockMvc.perform(delete(commentUrl(comment)).with(as(ADMIN_EMAIL)))
                .andExpect(status().isOk());

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void userCannotDeleteSomeoneElsesComment() throws Exception {
        UserEntity user = saveUser(USER_EMAIL, Role.USER);
        CommentEntity comment = saveComment(saveAd(user), user);
        saveUser(OTHER_EMAIL, Role.USER);

        mockMvc.perform(delete(commentUrl(comment)).with(as(OTHER_EMAIL)))
                .andExpect(status().isForbidden());
    }

    private String commentUrl(CommentEntity comment) {
        return "/ads/" + comment.getAd().getId() + "/comments/" + comment.getId();
    }
}
