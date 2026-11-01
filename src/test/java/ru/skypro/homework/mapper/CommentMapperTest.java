package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.skypro.homework.TestData;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {CommentMapperImpl.class, ImageUrlMapper.class})
class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    void toCommentTakesAuthorDataAndConvertsDate() {
        UserEntity user = TestData.user();
        Comment comment = commentMapper.toComment(TestData.comment(TestData.ad(user), user));

        assertThat(comment.getPk()).isEqualTo(100);
        assertThat(comment.getAuthor()).isEqualTo(1);
        assertThat(comment.getAuthorFirstName()).isEqualTo("Ivan");
        assertThat(comment.getAuthorImage()).isEqualTo("/images/avatar.png");
        assertThat(comment.getCreatedAt()).isEqualTo(TestData.CREATED_AT.toEpochMilli());
        assertThat(comment.getText()).isEqualTo("Is it still available?");
    }

    @Test
    void toCommentsCountsResults() {
        UserEntity user = TestData.user();
        CommentEntity comment = TestData.comment(TestData.ad(user), user);

        Comments comments = commentMapper.toComments(List.of(comment));

        assertThat(comments.getCount()).isEqualTo(1);
        assertThat(comments.getResults()).hasSize(1);
    }

    @Test
    void toEntityCopiesOnlyText() {
        CreateOrUpdateComment dto = new CreateOrUpdateComment();
        dto.setText("New comment text");

        CommentEntity entity = commentMapper.toEntity(dto);

        assertThat(entity.getText()).isEqualTo("New comment text");
        assertThat(entity.getId()).isNull();
        assertThat(entity.getAuthor()).isNull();
    }
}
