package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Список комментариев с их количеством.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Список комментариев")
public class Comments {

    @Schema(description = "общее количество комментариев")
    private Integer count = 0;

    @Schema(description = "комментарии")
    private List<Comment> results = new ArrayList<>();
}
