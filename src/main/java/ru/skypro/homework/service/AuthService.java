package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

/**
 * Вход и регистрация пользователей.
 */
public interface AuthService {

    /**
     * Проверяет логин и пароль.
     *
     * @param userName логин (email)
     * @param password пароль в открытом виде
     * @throws org.springframework.security.authentication.BadCredentialsException если логин или пароль неверны
     */
    void login(String userName, String password);

    /**
     * Регистрирует нового пользователя. Роль по умолчанию — USER.
     *
     * @param register данные регистрации
     * @return false, если пользователь с таким логином уже есть
     */
    boolean register(Register register);
}
