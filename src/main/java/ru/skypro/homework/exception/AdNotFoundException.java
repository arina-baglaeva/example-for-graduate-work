package ru.skypro.homework.exception;

/**
 * Объявление с указанным id не найдено.
 */
public class AdNotFoundException extends NotFoundException {

    public AdNotFoundException(Integer id) {
        super("Ad not found: " + id);
    }
}
