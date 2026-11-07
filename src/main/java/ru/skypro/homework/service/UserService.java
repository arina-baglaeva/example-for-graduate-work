package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

/**
 * Работа с профилем пользователя.
 */
public interface UserService {

    /**
     * @param email логин пользователя
     * @return профиль пользователя
     */
    User getUser(String email);

    /**
     * Обновляет имя, фамилию и телефон. Пустые поля не меняются.
     *
     * @param email      логин пользователя
     * @param updateUser новые данные
     * @return данные профиля после обновления
     */
    UpdateUser updateUser(String email, UpdateUser updateUser);

    /**
     * Меняет пароль после проверки текущего.
     *
     * @param email       логин пользователя
     * @param newPassword текущий и новый пароль
     * @throws ru.skypro.homework.exception.InvalidPasswordException если текущий пароль неверен
     */
    void setPassword(String email, NewPassword newPassword);

    /**
     * Сохраняет новый аватар, прежний файл удаляется.
     *
     * @param email логин пользователя
     * @param image файл картинки
     */
    void updateUserImage(String email, MultipartFile image);

    /**
     * @param email логин пользователя
     * @return сущность пользователя
     * @throws ru.skypro.homework.exception.UserNotFoundException если пользователя нет
     */
    UserEntity getUserEntity(String email);
}
