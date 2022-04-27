package com.project.reddit.mapper;

import com.project.reddit.dto.comment.*;
import com.project.reddit.model.message.Comment;
import com.project.reddit.model.message.CommentLikeDislike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    Comment toEntity(CommentRequest request);

    @Mapping(source = "comment.user", target = "userInfo")
    @Mapping(source = "comment.likeDislikes", target = "likedOrDislikedComments")
    CommentDto toDto(Comment comment);

    @Mapping(source = "likeOrDislike", target = "likedOrDisliked")
    LikedOrDislikedCommentsDto likedDislikedDto(CommentLikeDislike commentLikeDislike);

    CommentDto requestToCommentDto(CommentRequest request);

/*    @Mapping(target = "commentId", source = "comment")
    @Mapping(target = "liked", source = "likeOrDislike")
    LikedOrDislikedComments likedOrDislikedCommentsDto (CommentLikeDislike request);*/

}
