package com.project.reddit.kafka.service.generic.notifications.likedislike;

import com.project.reddit.kafka.service.generic.AbstractSendNotification;
import com.project.reddit.kafka.service.generic.GenericKafkaSender;
import com.project.reddit.kafka.service.generic.model.LikeDislikePostNotificationModel;
import org.springframework.stereotype.Component;

@Component
public class LikeOrDislikePostNotification extends AbstractSendNotification<LikeDislikePostNotificationModel> {
    public LikeOrDislikePostNotification(GenericKafkaSender<LikeDislikePostNotificationModel> genericKafkaSender) {
        super(genericKafkaSender);
    }

    @Override
    public void sendNotification(LikeDislikePostNotificationModel object, String topic) {
        super.sendNotification(object, topic);
    }
}
