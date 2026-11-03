package ru.skypro.homework.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * Общие настройки для всех мапперов: Spring-бины, ошибка компиляции при пропущенном поле,
 * при обновлении сущности пустые поля DTO не затирают значения.
 */
@MapperConfig(
        componentModel = "spring",
        uses = ImageUrlMapper.class,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapStructConfig {
}
