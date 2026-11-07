package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

/**
 * Работа с объявлениями.
 */
public interface AdService {

    /**
     * @return все объявления
     */
    Ads getAllAds();

    /**
     * Создаёт объявление с картинкой от имени пользователя.
     *
     * @param createOrUpdateAd заголовок, цена и описание
     * @param image            картинка объявления
     * @param email            логин автора
     * @return созданное объявление с id
     */
    Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, String email);

    /**
     * @param id id объявления
     * @return объявление с контактами автора
     * @throws ru.skypro.homework.exception.AdNotFoundException если объявления нет
     */
    ExtendedAd getAd(Integer id);

    /**
     * Удаляет объявление вместе с его комментариями. Доступно автору и администратору.
     *
     * @param id             id объявления
     * @param authentication текущий пользователь
     * @throws org.springframework.security.access.AccessDeniedException если нет прав
     */
    void removeAd(Integer id, Authentication authentication);

    /**
     * Обновляет объявление. Доступно автору и администратору.
     *
     * @param id               id объявления
     * @param createOrUpdateAd новые данные
     * @param authentication   текущий пользователь
     * @return обновлённое объявление
     * @throws org.springframework.security.access.AccessDeniedException если нет прав
     */
    Ad updateAd(Integer id, CreateOrUpdateAd createOrUpdateAd, Authentication authentication);

    /**
     * @param email логин пользователя
     * @return только объявления этого пользователя
     */
    Ads getMyAds(String email);

    /**
     * Заменяет картинку объявления. Доступно автору и администратору.
     *
     * @param id             id объявления
     * @param image          новая картинка
     * @param authentication текущий пользователь
     * @return содержимое новой картинки
     * @throws org.springframework.security.access.AccessDeniedException если нет прав
     */
    byte[] updateImage(Integer id, MultipartFile image, Authentication authentication);

    /**
     * @param id id объявления
     * @return сущность объявления
     * @throws ru.skypro.homework.exception.AdNotFoundException если объявления нет
     */
    AdEntity getAdEntity(Integer id);
}
