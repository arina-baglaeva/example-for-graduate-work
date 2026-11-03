package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdControllerTest extends IntegrationTest {

    private static final String AD_JSON =
            "{\"title\":\"Scooter\",\"price\":100,\"description\":\"Fast electric scooter\"}";

    @Test
    void getAllAdsWorksWithoutAuth() throws Exception {
        saveAd(saveUser(USER_EMAIL, Role.USER));

        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    void addAdWithoutAuthReturnsUnauthorized() throws Exception {
        mockMvc.perform(multipart("/ads").file(properties()).file(image()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addAdSavesAdOfCurrentUser() throws Exception {
        UserEntity user = saveUser(USER_EMAIL, Role.USER);

        mockMvc.perform(multipart("/ads").file(properties()).file(image()).with(as(USER_EMAIL)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pk").isNumber())
                .andExpect(jsonPath("$.author").value(user.getId()))
                .andExpect(jsonPath("$.title").value("Scooter"));
    }

    @Test
    void getAdsMeReturnsOnlyOwnAds() throws Exception {
        UserEntity user = saveUser(USER_EMAIL, Role.USER);
        saveAd(user);
        saveAd(saveUser(OTHER_EMAIL, Role.USER));

        mockMvc.perform(get("/ads/me").with(as(USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].author").value(user.getId()));
    }

    @Test
    void getAdReturnsAuthorContacts() throws Exception {
        AdEntity ad = saveAd(saveUser(USER_EMAIL, Role.USER));

        mockMvc.perform(get("/ads/" + ad.getId()).with(as(USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(USER_EMAIL))
                .andExpect(jsonPath("$.description").value("Good old bicycle"));
    }

    @Test
    void getMissingAdReturnsNotFound() throws Exception {
        saveUser(USER_EMAIL, Role.USER);

        mockMvc.perform(get("/ads/999").with(as(USER_EMAIL)))
                .andExpect(status().isNotFound());
    }

    @Test
    void authorCanUpdateOwnAd() throws Exception {
        AdEntity ad = saveAd(saveUser(USER_EMAIL, Role.USER));

        mockMvc.perform(patch("/ads/" + ad.getId()).with(as(USER_EMAIL))
                        .contentType(MediaType.APPLICATION_JSON).content(AD_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Scooter"));
    }

    @Test
    void userCannotUpdateSomeoneElsesAd() throws Exception {
        AdEntity ad = saveAd(saveUser(USER_EMAIL, Role.USER));
        saveUser(OTHER_EMAIL, Role.USER);

        mockMvc.perform(patch("/ads/" + ad.getId()).with(as(OTHER_EMAIL))
                        .contentType(MediaType.APPLICATION_JSON).content(AD_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanUpdateSomeoneElsesAd() throws Exception {
        AdEntity ad = saveAd(saveUser(USER_EMAIL, Role.USER));
        saveUser(ADMIN_EMAIL, Role.ADMIN);

        mockMvc.perform(patch("/ads/" + ad.getId()).with(as(ADMIN_EMAIL))
                        .contentType(MediaType.APPLICATION_JSON).content(AD_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void authorCanDeleteOwnAdWithComments() throws Exception {
        UserEntity user = saveUser(USER_EMAIL, Role.USER);
        AdEntity ad = saveAd(user);
        saveComment(ad, user);

        mockMvc.perform(delete("/ads/" + ad.getId()).with(as(USER_EMAIL)))
                .andExpect(status().isNoContent());

        assertThat(adRepository.findById(ad.getId())).isEmpty();
        assertThat(commentRepository.count()).isZero();
    }

    @Test
    void userCannotDeleteSomeoneElsesAd() throws Exception {
        AdEntity ad = saveAd(saveUser(USER_EMAIL, Role.USER));
        saveUser(OTHER_EMAIL, Role.USER);

        mockMvc.perform(delete("/ads/" + ad.getId()).with(as(OTHER_EMAIL)))
                .andExpect(status().isForbidden());

        assertThat(adRepository.findById(ad.getId())).isPresent();
    }

    private MockMultipartFile properties() {
        return new MockMultipartFile("properties", "", MediaType.APPLICATION_JSON_VALUE, AD_JSON.getBytes());
    }

    private MockMultipartFile image() {
        return new MockMultipartFile("image", "ad.png", MediaType.IMAGE_PNG_VALUE, new byte[]{1, 2, 3});
    }
}
