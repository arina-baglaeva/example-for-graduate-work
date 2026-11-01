package ru.skypro.homework.exception;

/**
 * Комментарий с указанным id не найден у объявления.
 */
public class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException(Integer adId, Integer commentId) {
        super("Comment " + commentId + " not found in ad " + adId);
    }
}
