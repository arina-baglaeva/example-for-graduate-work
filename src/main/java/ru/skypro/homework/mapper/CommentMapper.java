package ru.skypro.homework.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

/**
 * Маппинг комментария между сущностью и DTO.
 */
@Mapper(config = MapStructConfig.class)
public interface CommentMapper {

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorImage", source = "author.image", qualifiedByName = "toImageUrl")
    @Mapping(target = "createdAt", expression = "java(entity.getCreatedAt().toEpochMilli())")
    Comment toComment(CommentEntity entity);

    List<Comment> toCommentList(List<CommentEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "ad", ignore = true)
    @Mapping(target = "author", ignore = true)
    CommentEntity toEntity(CreateOrUpdateComment createOrUpdateComment);

    @InheritConfiguration(name = "toEntity")
    void updateEntity(CreateOrUpdateComment createOrUpdateComment, @MappingTarget CommentEntity entity);

    default Comments toComments(List<CommentEntity> entities) {
        List<Comment> results = toCommentList(entities);
        return new Comments(results.size(), results);
    }
}
