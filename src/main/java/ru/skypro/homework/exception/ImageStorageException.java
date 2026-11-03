package ru.skypro.homework.exception;

/**
 * Ошибка чтения или записи файла картинки на диске.
 */
public class ImageStorageException extends RuntimeException {

    public ImageStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
