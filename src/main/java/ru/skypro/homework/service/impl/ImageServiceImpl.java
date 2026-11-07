package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.exception.ImageStorageException;
import ru.skypro.homework.exception.InvalidImageException;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Картинки лежат в папке из настройки app.images.dir, в БД хранится только имя файла.
 */
@Service
public class ImageServiceImpl implements ImageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("png", "jpg", "jpeg", "gif", "webp");
    private static final String IMAGE_CONTENT_TYPE_PREFIX = "image/";

    private final Path imagesDir;

    public ImageServiceImpl(@Value("${app.images.dir}") String imagesDir) {
        this.imagesDir = Paths.get(imagesDir).toAbsolutePath().normalize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String save(MultipartFile image) {
        String fileName = UUID.randomUUID() + "." + getExtension(image);
        try {
            Files.createDirectories(imagesDir);
            image.transferTo(imagesDir.resolve(fileName));
        } catch (IOException e) {
            throw new ImageStorageException("Cannot save image " + fileName, e);
        }
        return fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String replace(MultipartFile image, String oldFileName) {
        String fileName = save(image);
        delete(oldFileName);
        return fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] load(String fileName) {
        Path file = resolve(fileName);
        if (!Files.isRegularFile(file)) {
            throw new ImageNotFoundException(fileName);
        }
        try {
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new ImageStorageException("Cannot read image " + fileName, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String fileName) {
        if (fileName == null) {
            return;
        }
        try {
            Files.deleteIfExists(resolve(fileName));
        } catch (IOException e) {
            throw new ImageStorageException("Cannot delete image " + fileName, e);
        }
    }

    private String getExtension(MultipartFile image) {
        String contentType = image.getContentType();
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        if (image.isEmpty()
                || contentType == null
                || !contentType.startsWith(IMAGE_CONTENT_TYPE_PREFIX)
                || extension == null
                || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase(Locale.ROOT))) {
            throw new InvalidImageException(image.getOriginalFilename());
        }
        return extension.toLowerCase(Locale.ROOT);
    }

    /**
     * Не даёт выйти за пределы папки с картинками через имя вида ../../file.
     */
    private Path resolve(String fileName) {
        Path file = imagesDir.resolve(fileName).normalize();
        if (!file.getParent().equals(imagesDir)) {
            throw new ImageNotFoundException(fileName);
        }
        return file;
    }
}
