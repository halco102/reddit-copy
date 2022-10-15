package com.project.reddit.dto.user.notification;

import com.project.reddit.dto.post.PostNotificationDto;
import com.project.reddit.dto.user.UserInfo;
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

}
