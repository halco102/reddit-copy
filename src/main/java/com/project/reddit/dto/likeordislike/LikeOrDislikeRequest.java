package com.project.reddit.dto.likeordislike;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeOrDislikeRequest {

    private Long id;

    @JsonProperty("likeOrDislike")
    private boolean likeOrDislike;

    @JsonProperty("postId")
    public Long getId() {
        return id;
    }
}
