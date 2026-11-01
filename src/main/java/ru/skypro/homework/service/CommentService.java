package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

/**
 * Работа с комментариями к объявлениям.
 */
public interface CommentService {

    /**
     * @param adId id объявления
     * @return комментарии объявления, новые сверху
     * @throws ru.skypro.homework.exception.AdNotFoundException если объявления нет
     */
    Comments getComments(Integer adId);

    /**
     * Добавляет комментарий от имени пользователя.
     *
     * @param adId                  id объявления
     * @param createOrUpdateComment текст комментария
     * @param email                 логин автора
     * @return созданный комментарий
     */
    Comment addComment(Integer adId, CreateOrUpdateComment createOrUpdateComment, String email);

    /**
     * Удаляет комментарий. Доступно автору и администратору.
     *
     * @param adId           id объявления
     * @param commentId      id комментария
     * @param authentication текущий пользователь
     * @throws org.springframework.security.access.AccessDeniedException если нет прав
     */
    void deleteComment(Integer adId, Integer commentId, Authentication authentication);

    /**
     * Меняет текст комментария. Доступно автору и администратору.
     *
     * @param adId                  id объявления
     * @param commentId             id комментария
     * @param createOrUpdateComment новый текст
     * @param authentication        текущий пользователь
     * @return обновлённый комментарий
     * @throws org.springframework.security.access.AccessDeniedException если нет прав
     */
    Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment,
                          Authentication authentication);
}
