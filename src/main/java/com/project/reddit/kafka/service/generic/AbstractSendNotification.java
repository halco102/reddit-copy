package com.project.reddit.kafka.service.generic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractSendNotification<T> implements INotification<T>{

    private final GenericKafkaSender<T> genericKafkaSender;

    @Override
    public void sendNotification(T object, String topic) {
        if (object == null) {
            return;
        }
        genericKafkaSender.sendMessageWithCallback(object, topic);
    }


}
