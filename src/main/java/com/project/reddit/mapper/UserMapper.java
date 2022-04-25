package com.project.reddit.mapper;

import com.project.reddit.dto.comment.LikedOrDislikedComments;
import com.project.reddit.dto.user.*;
import com.project.reddit.model.message.Comment;
import com.project.reddit.model.message.CommentLikeDislike;
import com.project.reddit.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import javax.persistence.MapsId;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User signupToEntity (UserSignupRequestDto signupRequestDto);

    UserSignupResponseDto signupResponseDto(User entity);

    @Mapping(target = "posts", source = "posts")
    UserProfileDto userProfileDto(User user);
    @Mapping(target = "likeOrDislike", source = "likeOrDislike")
    @Mapping(target = "commentDto", source = "comment")
    LikedOrDislikedComments likeOrDislike(CommentLikeDislike commentLikeDislike);

}
