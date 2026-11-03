package ru.skypro.homework.constants;

/**
 * Общие настройки веб-слоя.
 */
public final class WebConstants {

    /**
     * Адрес фронтенда, которому разрешены CORS-запросы.
     */
    public static final String FRONTEND_URL = "http://localhost:3000";

    /**
     * Начало пути, по которому фронтенд получает картинки.
     */
    public static final String IMAGE_URL_PREFIX = "/images/";

    private WebConstants() {
    }
}
