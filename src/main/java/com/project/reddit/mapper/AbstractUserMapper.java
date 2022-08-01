package com.project.reddit.mapper;

import com.project.reddit.dto.comment.LikedOrDislikedCommentsUser;
import com.project.reddit.dto.post.PostLikeOrDislikeDto;
import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.dto.user.follower.FollowDto;
import com.project.reddit.dto.user.login.UserLoginResponse;
import com.project.reddit.dto.user.signup.UserSignupRequestDto;
import com.project.reddit.dto.user.signup.UserSignupResponseDto;
import com.project.reddit.model.likedislike.CommentLikeOrDislike;
import com.project.reddit.model.likedislike.PostLikeOrDislike;
import com.project.reddit.model.user.User;
import com.project.reddit.model.user.follow.Follows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class AbstractUserMapper {

    public abstract User signupToEntity (UserSignupRequestDto signupRequestDto);

    public abstract UserSignupResponseDto signupResponseDto(User entity);

    @Mapping(target = "jwt", source = "token")
    public abstract UserLoginResponse userLoginResponseDto(String token);

    @Mappings({
            @Mapping(target = "posts", source = "posts"),
            @Mapping(target = "followingDto", expression = "java(following(user.getFollowing()))"),
            @Mapping(target = "followersDto", expression = "java(followers(user.getFollowers()))")

    })
    public abstract UserProfileDto userProfileDto(User user);

    protected Set<FollowDto> followers(Set<Follows> user) {
        Set<FollowDto> followDtos = new HashSet<>();

        if (user == null) {
            return followDtos;
        }

        for (Follows f : user) {
            followDtos.add( new FollowDto(f.getFrom().getId(), f.getFrom().getUsername(), f.getFrom().getEmail(), f.getFrom().getCreatedAt(), f.getFrom().getImageUrl()) );
        }

        return followDtos;
    }

    protected Set<FollowDto> following(Set<Follows> user) {
        Set<FollowDto> followDtos = new HashSet<>();

        if (user == null) {
            return followDtos;
        }

        for (Follows f : user) {
            followDtos.add( new FollowDto(f.getTo().getId(), f.getTo().getUsername(),
                    f.getTo().getEmail(),
                    f.getTo().getCreatedAt(),
                    f.getTo().getImageUrl()) );
        }

        return followDtos;
    }

    @Mappings({
            @Mapping(target = "likeOrDislike", source = "likeOrDislike"),
            @Mapping(target = "commentId", source = "comment.id"),
            @Mapping(source = "comment.post.id", target = "postId")
    })
    public abstract LikedOrDislikedCommentsUser likeOrDislike(CommentLikeOrDislike commentLikeDislike);

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "likeOrDislike", source = "likeOrDislike")
    public abstract PostLikeOrDislikeDto postLikeOrDislike(PostLikeOrDislike postLikeOrDislike);


}