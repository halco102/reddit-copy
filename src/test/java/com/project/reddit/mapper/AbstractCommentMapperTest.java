package com.project.reddit.mapper;

import com.project.reddit.dto.comment.CommentRequest;
import com.project.reddit.model.content.Post;
import com.project.reddit.model.likedislike.CommentLikeOrDislike;
import com.project.reddit.model.likedislike.EmbedableCommentLikeOrDislike;
import com.project.reddit.model.message.Comment;
import com.project.reddit.model.user.User;
import com.project.reddit.model.user.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
class AbstractCommentMapperTest {

    private CommentRequest commentRequest;

    private Comment comment;

    private CommentLikeOrDislike commentLikeOrDislike;

    private User user;

    private Post post;

    @BeforeEach
    public void beforeEachSetup() {
        user = new User(1L, "username", "password", "email@email.com", LocalDate.now(), "imageUrl", UserRole.ROLE_USER);
        post = new Post(1L, "title", "text", "image", true, user);

        commentRequest = new CommentRequest("text1", 1L, 1L, null);
        comment = new Comment(1L,"text1", LocalDate.now(), null, post, user, null, new ArrayList<>());
        commentLikeOrDislike = new CommentLikeOrDislike();
    }

    @Test
    void toEntityTest() {
        var toEntity = Mappers.getMapper(AbstractCommentMapper.class).toEntity(commentRequest);

        Assertions.assertEquals(toEntity.getText(), commentRequest.getText());
        Assertions.assertNull(toEntity.getParentId());
    }

    @Test
    void toDtoTest() {
        var toDto = Mappers.getMapper(AbstractCommentMapper.class).toDto(comment);

        Assertions.assertEquals(toDto.getId(), comment.getId());
        Assertions.assertEquals(toDto.getText(), comment.getText());
        Assertions.assertNull(toDto.getParentId());
        Assertions.assertEquals(toDto.getLikedOrDislikedComments().size(), 0);

    }

    @Test
    void testToDtoTestWithLikesAndDislikesOnComment() {
        CommentLikeOrDislike commentLikeOrDislike1 = new CommentLikeOrDislike(
                user, false,
                new EmbedableCommentLikeOrDislike(1L, 1L),
                comment
        );

        comment.setLikeDislikes(
                new ArrayList<>(Arrays.asList(
                       commentLikeOrDislike1
                ))
        );

        var toDto = Mappers.getMapper(AbstractCommentMapper.class).toDto(comment);


        Assertions.assertEquals(toDto.getId(), comment.getId());
        Assertions.assertEquals(toDto.getText(), comment.getText());
        Assertions.assertNull(toDto.getParentId());
        Assertions.assertEquals(toDto.getLikedOrDislikedComments().size(), 1);
        Assertions.assertEquals(toDto.getLikedOrDislikedComments().get(0).isLikedOrDisliked(), commentLikeOrDislike1.isLikeOrDislike());
        Assertions.assertEquals(toDto.getUserInfo().getId(), user.getId());
        Assertions.assertEquals(toDto.getUserInfo().getUsername(), user.getUsername());
        Assertions.assertEquals(toDto.getUserInfo().getImageUrl(), user.getImageUrl());
    }

    @Test
    void testToDtoWhenParentIdIsNotNull() {
        comment.setId(2L);
        comment.setParentId(1L);
        var toDto = Mappers.getMapper(AbstractCommentMapper.class).toDto(comment);

        Assertions.assertEquals(toDto.getParentId(), comment.getParentId());
        Assertions.assertEquals(toDto.getId(), comment.getId());
    }

    @Test
    void testLikeModelToEntity() {
        var likedOrDislikedCommentsDto = Mappers.getMapper(AbstractCommentMapper.class).likeModelToEntity(commentLikeOrDislike);

        Assertions.assertEquals(likedOrDislikedCommentsDto.isLikedOrDisliked(), commentLikeOrDislike.isLikeOrDislike());
    }
}