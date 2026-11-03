package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.constants.ValidationConstants;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Данные нового пользователя для регистрации.
 */
@Data
@Schema(description = "Данные для регистрации")
public class Register {

    @Size(min = 4, max = 32)
    @Schema(description = "логин", minLength = 4, maxLength = 32)
    private String username;

    @Size(min = 8, max = 16)
    @Schema(description = "пароль", minLength = 8, maxLength = 16)
    private String password;

    @Size(min = 2, max = 16)
    @Schema(description = "имя пользователя", minLength = 2, maxLength = 16)
    private String firstName;

    @Size(min = 2, max = 16)
    @Schema(description = "фамилия пользователя", minLength = 2, maxLength = 16)
    private String lastName;

    @Pattern(regexp = ValidationConstants.PHONE_PATTERN)
    @Schema(description = "телефон пользователя", pattern = ValidationConstants.PHONE_PATTERN)
    private String phone;

    @Schema(description = "роль пользователя")
    private Role role;
}
