package com.project.reddit.kafka.service;

import com.project.reddit.dto.post.PostDto;

public interface INotifications {

    //it is enough to just push postDto because it holds info about the post and the user who published it
    void sendNotificationToFollowers(Object object, String topic);

}
