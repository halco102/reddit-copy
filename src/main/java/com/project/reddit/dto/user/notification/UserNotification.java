package com.project.reddit.dto.user.notification;

import com.project.reddit.dto.post.PostNotificationDto;
import com.project.reddit.dto.user.UserInfo;
import com.project.reddit.model.content.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNotification {

    private UserInfo userInfo;

    private PostNotificationDto notifications;

    //workaround
    public UserNotification(Long userId, String  username, String userImageUrl, Long postId, String postTitle, String postImageUrl) {
        this.userInfo = new UserInfo(userId, username, userImageUrl);
        this.notifications = new PostNotificationDto(postId, postTitle, postImageUrl);
    }
}

