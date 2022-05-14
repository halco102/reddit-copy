package com.project.reddit.controller;

import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.dto.post.PostDto;
import com.project.reddit.model.content.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Controller
@Slf4j
public class MessageBrokerController {

    @MessageMapping("/post")
    @SendTo("/topic/post")
    public List<PostDto> savedNewPost(List<PostDto> posts) {
        log.info("A user saved a post, now we need to send his state to all subscribers");
        return posts;
    }

    @MessageMapping("/comment")
    @SendTo("/topic/comment")
    public List<CommentDto> savedNewComment(List<CommentDto> commentDtos) {
        log.info("A user posted a new comment, send his state to all subscribed users");
        // or myb call db ?
        return commentDtos;
    }

}
