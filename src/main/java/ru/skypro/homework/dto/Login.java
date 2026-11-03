package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Schema(description = "Данные для входа")
public class Login {

    @Size(min = 4, max = 32)
    @Schema(description = "логин", minLength = 4, maxLength = 32)
    private String username;

    @Size(min = 8, max = 16)
    @Schema(description = "пароль", minLength = 8, maxLength = 16)
    private String password;
}
