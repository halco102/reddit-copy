package com.project.reddit.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeOrDislikeDto {

    private Long postId;

    private boolean likeOrDislike;
}
