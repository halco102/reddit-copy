package com.project.reddit.kafka.service;

import com.project.reddit.kafka.model.Notification;

public interface FollowNotification {

    void sendNotificationToFollowers(Notification user, String topic);

}
