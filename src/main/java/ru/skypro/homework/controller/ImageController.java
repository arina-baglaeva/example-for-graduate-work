package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.constants.WebConstants;
import ru.skypro.homework.service.ImageService;

/**
 * Отдаёт фронтенду картинки по пути, который указан в поле image у DTO.
 */
@CrossOrigin(value = WebConstants.FRONTEND_URL)
@RestController
@Tag(name = "Картинки")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Получение картинки объявления или аватара")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping(WebConstants.IMAGE_URL_PREFIX + "{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        MediaType mediaType = MediaTypeFactory.getMediaType(fileName)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imageService.load(fileName));
    }
}
