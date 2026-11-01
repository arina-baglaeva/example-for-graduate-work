package ru.skypro.homework.mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.skypro.homework.constants.WebConstants;

/**
 * Превращает имя файла картинки из БД в путь, по которому её запрашивает фронтенд.
 */
@Component
public class ImageUrlMapper {

    /**
     * @param fileName имя файла картинки
     * @return путь вида /images/{fileName}
     */
    @Named("toImageUrl")
    public String toImageUrl(String fileName) {
        return WebConstants.IMAGE_URL_PREFIX + fileName;
    }
}
