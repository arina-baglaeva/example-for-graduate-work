package ru.skypro.homework.constants;

/**
 * Общие правила валидации входящих данных.
 */
public final class ValidationConstants {

    /**
     * Формат телефона из спецификации: +7 (000) 000-00-00 и его варианты без скобок, пробелов и дефисов.
     */
    public static final String PHONE_PATTERN = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}";

    private ValidationConstants() {
    }
}
