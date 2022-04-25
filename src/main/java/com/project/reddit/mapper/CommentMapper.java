package com.project.reddit.mapper;

import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.dto.comment.CommentRequest;
import com.project.reddit.dto.comment.LikedOrDislikedComments;
import com.project.reddit.dto.comment.OrganizeCommentsInPost;
import com.project.reddit.model.message.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    Comment toEntity(CommentRequest request);

    @Mapping(source = "comment.user", target = "userInfo")
    CommentDto toDto(Comment comment);

    CommentDto requestToCommentDto(CommentRequest request);


}
