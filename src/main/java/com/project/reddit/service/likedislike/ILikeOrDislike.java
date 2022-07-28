package com.project.reddit.service.likedislike;

import com.project.reddit.dto.likeordislike.LikeOrDislikeRequest;

public interface ILikeOrDislike<T, S extends LikeOrDislikeRequest> {

    T likeOrDislike(S request);

}
