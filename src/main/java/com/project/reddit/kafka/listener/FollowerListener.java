package com.project.reddit.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FollowerListener {

    public void sendMsg(ConsumerRecord<String, Object> userConsumerRecord, Acknowledgment ack) {

        log.info("Kafka listener", userConsumerRecord.value());

        ack.acknowledge();

    }


}
