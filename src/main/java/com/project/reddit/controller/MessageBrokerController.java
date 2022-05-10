/*
package com.project.reddit.controller;

import com.project.reddit.dto.post.PostDto;
import com.project.reddit.model.content.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@Slf4j
public class MessageBrokerController {

    @MessageMapping("/post")
    @SendTo("/topic/post")
    public PostDto sendMessage(PostDto post) {
        log.info(String.valueOf(post));
        return post;
    }

}
*/
