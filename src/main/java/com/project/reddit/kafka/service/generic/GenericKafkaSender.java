package com.project.reddit.kafka.service.generic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenericKafkaSender<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;

    public void sendMessageWithCallback(T message, String topic) {

        System.out.println(message);
        ListenableFuture<SendResult<String, T>> future =
                kafkaTemplate.send(topic, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onSuccess(SendResult<String, T> result) {
                log.info("Message [{}] delivered with offset {}",
                        message,
                        result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.warn("Unable to deliver message [{}]. {}",
                        message.toString(),
                        ex.getMessage());
            }
        });
    }

}