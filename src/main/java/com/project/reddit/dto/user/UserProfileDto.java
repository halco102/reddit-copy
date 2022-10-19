package com.project.reddit.dto.user;

import com.project.reddit.dto.comment.LikedOrDislikedCommentsUser;
import com.project.reddit.dto.comment.UserProfileCommentsWithPostId;
import com.project.reddit.dto.post.PostForFrontPageDto;
import com.project.reddit.dto.post.PostLikeOrDislikeDto;
import com.project.reddit.dto.user.follower.FollowDto;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@ToString
public class UserProfileDto extends UserAbstract{

    // what user posted
    //private List<PostForFrontPageDto> posts;
    private List<PostForFrontPageDto> posts;

    //private List<CommentDto> comments;
    private List<UserProfileCommentsWithPostId> commentsPosts;

    // comments that user liked
    private List<LikedOrDislikedCommentsUser> likedOrDislikedComments;

    //posts that user liked
    private List<PostLikeOrDislikeDto> postLikeOrDislikeDtos;

    private Set<FollowDto> followersDtos;

    private Set<FollowDto> followingDtos;

    public UserProfileDto() {
    }
    public UserProfileDto(Long id, String username, String email, LocalDate createdAt, String imageUrl, List<PostForFrontPageDto> posts, List<UserProfileCommentsWithPostId> commentsPosts, List<LikedOrDislikedCommentsUser> likedOrDislikedComments, List<PostLikeOrDislikeDto> postLikeOrDislikeDtos) {
        super(id, username, email, createdAt, imageUrl);
        this.posts = posts;
        this.commentsPosts = commentsPosts;
        this.likedOrDislikedComments = likedOrDislikedComments;
        this.postLikeOrDislikeDtos = postLikeOrDislikeDtos;
    }

    public UserProfileDto(List<PostForFrontPageDto> posts, List<UserProfileCommentsWithPostId> commentsPosts, List<LikedOrDislikedCommentsUser> likedOrDislikedComments, List<PostLikeOrDislikeDto> postLikeOrDislikeDtos) {
        this.posts = posts;
        this.commentsPosts = commentsPosts;
        this.likedOrDislikedComments = likedOrDislikedComments;
        this.postLikeOrDislikeDtos = postLikeOrDislikeDtos;
    }


}
