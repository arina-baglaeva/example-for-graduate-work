package ru.skypro.homework.exception;

/**
 * При смене пароля указан неверный текущий пароль.
 */
public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Current password is incorrect");
    }
}
