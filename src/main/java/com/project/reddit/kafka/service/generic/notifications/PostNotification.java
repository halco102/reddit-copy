package com.project.reddit.kafka.service.generic.notifications;

import com.project.reddit.dto.post.PostDto;
import com.project.reddit.kafka.service.generic.GenericKafkaSender;
import com.project.reddit.kafka.service.generic.AbstractSendNotification;
import org.springframework.stereotype.Component;

@Component
public class PostNotification extends AbstractSendNotification<PostDto> {

    public PostNotification(GenericKafkaSender<PostDto> genericKafkaSender) {
        super(genericKafkaSender);
    }

    @Override
    public void sendNotification(PostDto object, String topic) {
        super.sendNotification(object, topic);
    }

}
