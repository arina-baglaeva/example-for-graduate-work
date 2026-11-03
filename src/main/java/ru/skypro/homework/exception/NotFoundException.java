package ru.skypro.homework.exception;

/**
 * Запрошенная запись не найдена в БД. Обработчик ошибок отвечает на неё кодом 404.
 */
public abstract class NotFoundException extends RuntimeException {

    protected NotFoundException(String message) {
        super(message);
    }
}
