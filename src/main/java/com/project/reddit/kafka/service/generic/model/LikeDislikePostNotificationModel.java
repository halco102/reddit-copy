package com.project.reddit.kafka.service.generic.model;

import com.project.reddit.dto.post.PostDto;
import com.project.reddit.dto.post.PostLikeOrDislikeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDislikePostNotificationModel {

    private PostDto postDto;

    private String eventName;


}
