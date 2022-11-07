package com.project.reddit.kafka.service.generic.notifications;

import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.kafka.service.generic.AbstractSendNotification;
import com.project.reddit.kafka.service.generic.GenericKafkaSender;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserProfileNotification extends AbstractSendNotification<UserProfileDto> {
    public UpdateUserProfileNotification(GenericKafkaSender<UserProfileDto> genericKafkaSender) {
        super(genericKafkaSender);
    }

    @Override
    public void sendNotification(UserProfileDto object, String topic) {
        super.sendNotification(object, topic);
    }
}
