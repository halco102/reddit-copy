package com.project.reddit.mapper;

import com.project.reddit.dto.comment.LikedOrDislikedCommentsUser;
import com.project.reddit.dto.post.PostDto;
import com.project.reddit.dto.post.PostLikeOrDislikeDto;
import com.project.reddit.dto.user.UserInfo;
import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.dto.user.follower.FollowDto;
import com.project.reddit.dto.user.login.UserLoginResponse;
import com.project.reddit.dto.user.notification.UserNotification;
import com.project.reddit.dto.user.signup.UserSignupRequestDto;
import com.project.reddit.dto.user.signup.UserSignupResponseDto;
import com.project.reddit.model.content.Post;
import com.project.reddit.model.likedislike.CommentLikeOrDislike;
import com.project.reddit.model.likedislike.PostLikeOrDislike;
import com.project.reddit.model.user.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {AbstractPostMapper.class})
public abstract class AbstractUserMapper {

    public abstract User signupToEntity (UserSignupRequestDto signupRequestDto);

    public abstract UserSignupResponseDto signupResponseDto(User entity);

    @Mapping(target = "jwt", source = "token")
    public abstract UserLoginResponse userLoginResponseDto(String token);


    @Mappings({
            @Mapping(target = "posts", source = "posts"),
            @Mapping(target = "followingDtos", source = "user.following"),
            @Mapping(target = "followersDtos", source = "user.followers")
    })
    public abstract UserProfileDto userProfileDto(User user);


    public abstract FollowDto followDto(User user);

    public abstract UserInfo userToUserInfo(User user);

    @Mappings({
            @Mapping(target = "likeOrDislike", source = "likeOrDislike"),
            @Mapping(target = "commentId", source = "comment.id"),
            @Mapping(source = "comment.post.id", target = "postId")
    })
    public abstract LikedOrDislikedCommentsUser likeOrDislike(CommentLikeOrDislike commentLikeDislike);

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "likeOrDislike", source = "likeOrDislike")
    public abstract PostLikeOrDislikeDto postLikeOrDislike(PostLikeOrDislike postLikeOrDislike);


    @Mappings({
            @Mapping(target = "notifications", source = "post"),
            @Mapping(target = "userInfo", source = "post.user")
    })
    public abstract UserNotification userNotificationFromPost(Post post);

    @Mappings({
            @Mapping(target = "notifications", source = "post"),
            @Mapping(target = "userInfo", source = "post.postedBy")
    })
    public abstract UserNotification userNotificationFromPostDto(PostDto post);

}
