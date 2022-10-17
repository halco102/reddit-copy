package com.project.reddit.kafka.service.generic.notifications;

import com.project.reddit.kafka.service.generic.AbstractSendNotification;
import com.project.reddit.kafka.service.generic.GenericKafkaSender;
import com.project.reddit.kafka.service.generic.model.PostCommentNotificationModel;
import org.springframework.stereotype.Component;

@Component
public class CommentPostedNotification extends AbstractSendNotification<PostCommentNotificationModel> {
    public CommentPostedNotification(GenericKafkaSender<PostCommentNotificationModel> genericKafkaSender) {
        super(genericKafkaSender);
    }

    @Override
    public void sendNotification(PostCommentNotificationModel object, String topic) {
        super.sendNotification(object, topic);
    }
}
