package com.project.reddit.service.post;

import com.project.reddit.dto.post.PostDto;
import com.project.reddit.dto.post.PostLikeOrDislikeRequest;

import java.util.List;

public interface PostLikeDislike {
    PostDto saveLikeOrDislikeForPost(PostLikeOrDislikeRequest request);
    List<PostDto> sortPostByNumberOfLikes();
    List<PostDto> sortPostByNumberOfDislikes();

}
