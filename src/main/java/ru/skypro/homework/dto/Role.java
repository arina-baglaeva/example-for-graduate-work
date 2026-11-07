package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Роль пользователя: обычный пользователь или администратор.
 */
@Schema(description = "Роль пользователя")
public enum Role {
    USER, ADMIN
}
