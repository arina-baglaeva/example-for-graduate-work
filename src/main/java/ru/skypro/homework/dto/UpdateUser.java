package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.constants.ValidationConstants;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "Обновление данных пользователя")
public class UpdateUser {

    @Size(min = 3, max = 10)
    @Schema(description = "имя пользователя", minLength = 3, maxLength = 10)
    private String firstName;

    @Size(min = 3, max = 10)
    @Schema(description = "фамилия пользователя", minLength = 3, maxLength = 10)
    private String lastName;

    @Pattern(regexp = ValidationConstants.PHONE_PATTERN)
    @Schema(description = "телефон пользователя", pattern = ValidationConstants.PHONE_PATTERN)
    private String phone;
}
