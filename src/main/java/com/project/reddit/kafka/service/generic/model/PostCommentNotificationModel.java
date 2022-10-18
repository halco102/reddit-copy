package com.project.reddit.kafka.service.generic.model;

import com.project.reddit.dto.comment.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentNotificationModel {

    private CommentDto commentDto;

    private Long postId;

}
