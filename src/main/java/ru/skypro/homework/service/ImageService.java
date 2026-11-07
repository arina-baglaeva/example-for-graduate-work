package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Хранение картинок объявлений и аватаров в файловой системе.
 */
public interface ImageService {

    /**
     * Сохраняет картинку под новым уникальным именем.
     *
     * @param image загруженный файл
     * @return имя сохранённого файла
     * @throws ru.skypro.homework.exception.InvalidImageException если файл пустой или не является картинкой
     */
    String save(MultipartFile image);

    /**
     * Сохраняет новую картинку и удаляет старую.
     *
     * @param image       загруженный файл
     * @param oldFileName имя прежнего файла, может быть null
     * @return имя сохранённого файла
     */
    String replace(MultipartFile image, String oldFileName);

    /**
     * @param fileName имя файла
     * @return содержимое картинки
     * @throws ru.skypro.homework.exception.ImageNotFoundException если файла нет
     */
    byte[] load(String fileName);

    /**
     * Удаляет файл картинки, если он есть.
     *
     * @param fileName имя файла, может быть null
     */
    void delete(String fileName);
}
