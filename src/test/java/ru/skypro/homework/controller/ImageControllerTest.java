package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ImageControllerTest extends IntegrationTest {

    private static final byte[] IMAGE_BYTES = {1, 2, 3, 4};
    private static final String AD_JSON =
            "{\"title\":\"Scooter\",\"price\":100,\"description\":\"Fast electric scooter\"}";

    @Test
    void uploadedAvatarIsAvailableByUrlFromProfile() throws Exception {
        saveUser(USER_EMAIL, Role.USER);

        mockMvc.perform(multipart(HttpMethod.PATCH, "/users/me/image").file(image()).with(as(USER_EMAIL)))
                .andExpect(status().isOk());

        String imageUrl = userRepository.findByEmail(USER_EMAIL).map(UserEntity::getImage)
                .map(fileName -> "/images/" + fileName).orElseThrow();
        mockMvc.perform(get("/users/me").with(as(USER_EMAIL)))
                .andExpect(jsonPath("$.image").value(imageUrl));
        mockMvc.perform(get(imageUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(IMAGE_BYTES));
    }

    @Test
    void uploadNotImageReturnsBadRequest() throws Exception {
        saveUser(USER_EMAIL, Role.USER);
        MockMultipartFile text = new MockMultipartFile("image", "notes.txt", MediaType.TEXT_PLAIN_VALUE,
                IMAGE_BYTES);

        mockMvc.perform(multipart(HttpMethod.PATCH, "/users/me/image").file(text).with(as(USER_EMAIL)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createdAdHasImageUrl() throws Exception {
        saveUser(USER_EMAIL, Role.USER);
        MockMultipartFile properties = new MockMultipartFile("properties", "", MediaType.APPLICATION_JSON_VALUE,
                AD_JSON.getBytes());

        mockMvc.perform(multipart("/ads").file(properties).file(image()).with(as(USER_EMAIL)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.image").value(startsWith("/images/")));
    }

    @Test
    void authorCanUpdateAdImage() throws Exception {
        AdEntity ad = saveAd(saveUser(USER_EMAIL, Role.USER));

        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/" + ad.getId() + "/image").file(image())
                        .with(as(USER_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().bytes(IMAGE_BYTES));

        assertThat(adRepository.findById(ad.getId()).orElseThrow().getImage()).endsWith(".png");
    }

    @Test
    void userCannotUpdateSomeoneElsesAdImage() throws Exception {
        AdEntity ad = saveAd(saveUser(USER_EMAIL, Role.USER));
        saveUser(OTHER_EMAIL, Role.USER);

        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/" + ad.getId() + "/image").file(image())
                        .with(as(OTHER_EMAIL)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deletingAdRemovesItsImage() throws Exception {
        AdEntity ad = saveAd(saveUser(USER_EMAIL, Role.USER));
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/" + ad.getId() + "/image").file(image())
                .with(as(USER_EMAIL)));
        String imageUrl = "/images/" + adRepository.findById(ad.getId()).orElseThrow().getImage();

        mockMvc.perform(delete("/ads/" + ad.getId()).with(as(USER_EMAIL)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(imageUrl))
                .andExpect(status().isNotFound());
    }

    @Test
    void missingImageReturnsNotFound() throws Exception {
        mockMvc.perform(get("/images/missing.png"))
                .andExpect(status().isNotFound());
    }

    private MockMultipartFile image() {
        return new MockMultipartFile("image", "photo.png", MediaType.IMAGE_PNG_VALUE, IMAGE_BYTES);
    }
}
