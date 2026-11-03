package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Текст нового или изменённого комментария.
 */
@Data
@Schema(description = "Создание или обновление комментария")
public class CreateOrUpdateComment {

    @NotNull
    @Size(min = 8, max = 64)
    @Schema(description = "текст комментария", minLength = 8, maxLength = 64,
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String text;
}
