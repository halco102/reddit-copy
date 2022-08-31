package com.project.reddit.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {

    @Bean
    public NewTopic notificationsTopic() {
        return TopicBuilder.name("followers-notification").partitions(1).build();
    }

    @Bean
    public NewTopic postNotifications() {
        return TopicBuilder.name("post-notification").partitions(1).build();
    }

    @Bean
    public NewTopic commentMessages() {
        return TopicBuilder.name("comment-messages").partitions(1).build();
    }

    @Bean
    public NewTopic userProfileNotifications() {
        return TopicBuilder.name("user-profile-notification").partitions(1).build();
    }

}
