package com.project.reddit.kafka.service.generic.notifications;

import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.kafka.service.generic.AbstractSendNotification;
import com.project.reddit.kafka.service.generic.GenericKafkaSender;
import org.springframework.stereotype.Component;

@Component
public class DeleteCommentNotification extends AbstractSendNotification<Long> {
    public DeleteCommentNotification(GenericKafkaSender<Long> genericKafkaSender) {
        super(genericKafkaSender);
    }

    @Override
    public void sendNotification(Long object, String topic) {
        super.sendNotification(object, topic);
    }
}
