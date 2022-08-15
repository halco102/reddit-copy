package com.project.reddit.kafka.service;

import com.project.reddit.dto.post.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendNotification implements INotifications {

    private final KafkaSender kafkaSender;

    @Override
    public void sendNotificationToFollowers(Object object, String topic) {

        if (object == null) {
            log.warn("REeeeee its null");
            return;
        }

        // dovoljno je samo poslati na topic i svi da slusaju taj topic
        // znaci kada user posta nesto, salje na odredjeni topic i svi slusaju taj topic
        // trebaju samo znati od koga dolazi poruka, tj u ovom slucaju da znaju userInfo ko je postao i sta je postao da im dodje u notifikacijama
        kafkaSender.sendMessageWithCallback(object, topic);
    }
}
