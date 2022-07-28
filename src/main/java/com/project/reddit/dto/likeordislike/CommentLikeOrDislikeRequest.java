package com.project.reddit.dto.likeordislike;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikeOrDislikeRequest extends LikeOrDislikeRequest {

    private Long userId;

    @Override
    @JsonProperty("commentId")
    public Long getId() {
        return super.getId();
    }

}
