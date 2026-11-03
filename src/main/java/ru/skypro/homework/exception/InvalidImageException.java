package ru.skypro.homework.exception;

/**
 * Загруженный файл пустой или не является картинкой допустимого формата.
 */
public class InvalidImageException extends RuntimeException {

    public InvalidImageException(String fileName) {
        super("File is not a supported image: " + fileName);
    }
}
