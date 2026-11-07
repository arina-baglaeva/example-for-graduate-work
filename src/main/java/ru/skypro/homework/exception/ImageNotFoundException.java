package ru.skypro.homework.exception;

/**
 * Файл картинки с указанным именем не найден.
 */
public class ImageNotFoundException extends NotFoundException {

    public ImageNotFoundException(String fileName) {
        super("Image not found: " + fileName);
    }
}
