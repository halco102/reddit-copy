package com.project.reddit.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeOrDislikeRequest {

    private Long postId;

    @JsonProperty("likeOrDislike")
    private boolean likeOrDislike;

}
