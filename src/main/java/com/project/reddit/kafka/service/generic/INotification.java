package com.project.reddit.kafka.service.generic;

public interface INotification<T>{

    void sendNotification(T object, String topic);

}
