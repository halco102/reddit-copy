package com.project.reddit.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikedOrDislikedCommentsUser {

    private Long postId;

    private Long commentId;

    private boolean likeOrDislike;

}
