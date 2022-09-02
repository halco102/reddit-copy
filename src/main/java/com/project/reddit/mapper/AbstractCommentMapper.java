package com.project.reddit.mapper;

import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.dto.comment.CommentRequest;
import com.project.reddit.dto.comment.LikedOrDislikedCommentsDto;
import com.project.reddit.model.likedislike.CommentLikeOrDislike;
import com.project.reddit.model.message.Comment;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class AbstractCommentMapper {

    public abstract Comment toEntity(CommentRequest request);

    @Mappings({
            @Mapping(source = "comment.user", target = "userInfo"),
            @Mapping(source = "likeDislikes", target = "likedOrDislikedComments")
    })
    public abstract CommentDto toDto(Comment comment);


    @Mapping(target = "likedOrDisliked", source = "likeOrDislike")
    public abstract LikedOrDislikedCommentsDto likeModelToEntity(CommentLikeOrDislike ld);

}
