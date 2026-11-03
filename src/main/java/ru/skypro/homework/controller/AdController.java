package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.constants.WebConstants;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import javax.validation.Valid;

/**
 * Работа с объявлениями.
 */
@CrossOrigin(value = WebConstants.FRONTEND_URL)
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdController {

    @Operation(summary = "Получение всех объявлений")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = Ads.class)))
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(new Ads());
    }

    @Operation(summary = "Добавление объявления")
    @ApiResponse(responseCode = "201", description = "Created",
            content = @Content(schema = @Schema(implementation = Ad.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(@Valid @RequestPart("properties") CreateOrUpdateAd properties,
                                    @RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Ad());
    }

    @Operation(summary = "Получение информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = ExtendedAd.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {
        return ResponseEntity.ok(new ExtendedAd());
    }

    @Operation(summary = "Удаление объявления")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Integer id) {
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = Ad.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable Integer id,
                                        @Valid @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return ResponseEntity.ok(new Ad());
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = Ads.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe() {
        return ResponseEntity.ok(new Ads());
    }

    @Operation(summary = "Обновление картинки объявления")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PatchMapping(value = "/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id,
                                              @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(new byte[0]);
    }
}
