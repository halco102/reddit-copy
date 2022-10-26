package com.project.reddit.kafka.service.generic.model;

import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.dto.comment.LikedOrDislikedCommentsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDislikeCommentNotificationModel {

    private CommentDto commentDto;

    private String eventName;

    private Long postId;

}
