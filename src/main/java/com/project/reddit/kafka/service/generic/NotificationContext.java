package com.project.reddit.kafka.service.generic;

import com.project.reddit.constants.KafkaNotifications;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.kafka.service.generic.notifications.CommentPostedNotification;
import com.project.reddit.kafka.service.generic.notifications.DeleteCommentNotification;
import com.project.reddit.kafka.service.generic.notifications.FollowerNotification;
import com.project.reddit.kafka.service.generic.notifications.PostNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NotificationContext {
    private final Map<KafkaNotifications, INotification> map = new HashMap<>();

    public NotificationContext(List<INotification> notifications) {
        for (INotification notification: notifications
        ) {
            if (notification instanceof FollowerNotification) {
                map.put(KafkaNotifications.FOLLOWER_NOTIFICATION, notification);
            }else if (notification instanceof PostNotification){
                map.put(KafkaNotifications.POST_NOTIFICATION, notification);
            }else if (notification instanceof DeleteCommentNotification) {
                map.put(KafkaNotifications.DELETE_COMMENT_NOTIFICATION, notification);
            }else if (notification instanceof CommentPostedNotification) {
                map.put(KafkaNotifications.COMMENT_NOTIFICATION, notification);
            }else {
                throw new NotFoundException("Error");
            }
        }
    }

    public void sendMessageToKafka(KafkaNotifications notificationType, Object object, String topic) {
         map.get(notificationType).sendNotification(object, topic);
    }
}
