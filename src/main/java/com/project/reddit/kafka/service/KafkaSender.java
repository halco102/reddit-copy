package com.project.reddit.kafka.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaSender {

    private final KafkaTemplate<Object, String> kafkaTemplate;

    private String objectToJson(Object o) {
        var gson = new Gson();

        return gson.toJson(o);
    }

    public void sendMessageWithCallback(Object message, String topic) {

        System.out.println(message);
        ListenableFuture<SendResult<Object, String>> future =
                kafkaTemplate.send(topic, objectToJson(message));

        future.addCallback(new ListenableFutureCallback<SendResult<Object, String>>() {
            @Override
            public void onSuccess(SendResult<Object, String> result) {
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
