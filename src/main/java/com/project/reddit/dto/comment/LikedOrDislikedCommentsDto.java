package com.project.reddit.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikedOrDislikedCommentsDto {

    private boolean isLikedOrDisliked;

}
