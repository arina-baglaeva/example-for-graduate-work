package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.security.AccessChecker;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.Instant;

/**
 * Комментарии хранятся в таблице comments, права на изменение проверяет {@link AccessChecker}.
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AdService adService;
    private final UserService userService;
    private final AccessChecker accessChecker;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Comments getComments(Integer adId) {
        adService.getAdEntity(adId);
        return commentMapper.toComments(commentRepository.findAllByAdIdOrderByCreatedAtDesc(adId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Comment addComment(Integer adId, CreateOrUpdateComment createOrUpdateComment, String email) {
        CommentEntity comment = commentMapper.toEntity(createOrUpdateComment);
        comment.setAd(adService.getAdEntity(adId));
        comment.setAuthor(userService.getUserEntity(email));
        comment.setCreatedAt(Instant.now());
        return commentMapper.toComment(commentRepository.save(comment));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteComment(Integer adId, Integer commentId, Authentication authentication) {
        CommentEntity comment = getComment(adId, commentId);
        accessChecker.checkCanModify(comment.getAuthor(), authentication);
        commentRepository.delete(comment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment,
                                 Authentication authentication) {
        CommentEntity comment = getComment(adId, commentId);
        accessChecker.checkCanModify(comment.getAuthor(), authentication);
        commentMapper.updateEntity(createOrUpdateComment, comment);
        return commentMapper.toComment(commentRepository.save(comment));
    }

    private CommentEntity getComment(Integer adId, Integer commentId) {
        return commentRepository.findByIdAndAdId(commentId, adId)
                .orElseThrow(() -> new CommentNotFoundException(adId, commentId));
    }
}
