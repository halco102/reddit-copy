package com.project.reddit.kafka.service.generic.notifications;

import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.kafka.service.generic.AbstractSendNotification;
import com.project.reddit.kafka.service.generic.GenericKafkaSender;
import org.springframework.stereotype.Component;

@Component
public class DeleteCommentNotification extends AbstractSendNotification<CommentDto> {
    public DeleteCommentNotification(GenericKafkaSender<CommentDto> genericKafkaSender) {
        super(genericKafkaSender);
    }

    @Override
    public void sendNotification(CommentDto object, String topic) {
        super.sendNotification(object, topic);
    }
}
