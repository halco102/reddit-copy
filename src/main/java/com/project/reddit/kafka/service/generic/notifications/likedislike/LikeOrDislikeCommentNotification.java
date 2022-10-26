package com.project.reddit.kafka.service.generic.notifications.likedislike;

import com.project.reddit.kafka.service.generic.AbstractSendNotification;
import com.project.reddit.kafka.service.generic.GenericKafkaSender;
import com.project.reddit.kafka.service.generic.model.LikeDislikeCommentNotificationModel;
import org.springframework.stereotype.Component;

@Component
public class LikeOrDislikeCommentNotification extends AbstractSendNotification<LikeDislikeCommentNotificationModel> {
    public LikeOrDislikeCommentNotification(GenericKafkaSender<LikeDislikeCommentNotificationModel> genericKafkaSender) {
        super(genericKafkaSender);
    }

    @Override
    public void sendNotification(LikeDislikeCommentNotificationModel object, String topic) {
        super.sendNotification(object, topic);
    }
}
