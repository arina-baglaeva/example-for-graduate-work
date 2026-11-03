package ru.skypro.homework.exception;

/**
 * Пользователь с указанным логином не найден.
 */
public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String email) {
        super("User not found: " + email);
    }
}
