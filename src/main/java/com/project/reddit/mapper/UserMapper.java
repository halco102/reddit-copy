package com.project.reddit.mapper;

import com.project.reddit.dto.comment.LikedOrDislikedCommentsUser;
import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.dto.user.login.UserLoginResponse;
import com.project.reddit.dto.user.signup.UserSignupRequestDto;
import com.project.reddit.dto.user.signup.UserSignupResponseDto;
import com.project.reddit.model.message.CommentLikeDislike;
import com.project.reddit.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { PostMapper.class })
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User signupToEntity (UserSignupRequestDto signupRequestDto);

    UserSignupResponseDto signupResponseDto(User entity);

    @Mapping(target = "userProfileDto", source = "user")
    @Mapping(target = "jwt", source = "token")
    UserLoginResponse userLoginResponseDto(User user, String token);

    @Mapping(target = "posts", source = "posts")
    UserProfileDto userProfileDto(User user);
    @Mapping(target = "likeOrDislike", source = "likeOrDislike")
    @Mapping(target = "commentDto", source = "comment")
    LikedOrDislikedCommentsUser likeOrDislike(CommentLikeDislike commentLikeDislike);

}
