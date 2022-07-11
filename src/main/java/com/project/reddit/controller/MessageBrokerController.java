package com.project.reddit.controller;

import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.dto.post.PostDto;
import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.service.CommentService;
import com.project.reddit.service.post.PostService;
import com.project.reddit.service.sorting.SortingCommentsInterface;
import com.project.reddit.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@Slf4j
public class MessageBrokerController {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    SortingCommentsInterface sortingCommentsInterface;

    @Autowired
    CommentService commentService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/post")
    @SendTo("/topic/post")
    @Transactional
    public List<PostDto> savedNewPost(List<PostDto> postDtos) {
        log.info("A user saved a post, now we need to send his state to all subscribers");
        if (postDtos.isEmpty() || !postDtos.isEmpty()){
            return this.postService.getAllPosts();
        }
        return null;
    }

    @MessageMapping("/post/delete")
    @SendTo("/topic/post")
    @Transactional
    public List<PostDto> deletedPost(String deleted) {
        log.info("Post was deleted");
        if (deleted.matches("POST_DELETED")) {
            var temp = postService.getAllPosts();
            return temp;
        }
        log.info("POST_DELETED NULL");
        return null;
    }

    @MessageMapping("/comment")
    @SendTo("/topic/comment")
    @Transactional
    public List<CommentDto> savedNewComment(List<CommentDto> commentDtos) {
        log.info("A user posted a new comment, send his state to all subscribed users");
        // or myb call db ?
        return sortingCommentsInterface.sortingComments(commentDtos);
    }

    @MessageMapping("/comment/delete")
    @SendTo("/topic/comment")
    @Transactional
    public List<CommentDto> deletedComment(Long postId) {
        log.info("A comment was deleted");
        return this.commentService.getAllCommentsByPostId(postId);
    }

    @MessageMapping("/user/update")
    @SendTo("/topic/user")
    @Transactional
    public UserProfileDto userProfileDtoChanged(UserProfileDto userProfileDto) {
        log.info("Specific user" + userProfileDto);
        //simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/topic/user", this.userService.getUserProfileByUsernameOrId(null, id));
        messagingTemplate.convertAndSend("/topic/post", postService.getAllPosts());
        return this.userService.getUserProfileByUsernameOrId(null, userProfileDto.getId());
    }

}
