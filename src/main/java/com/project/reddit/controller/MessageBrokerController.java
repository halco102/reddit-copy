package com.project.reddit.controller;

import com.google.gson.Gson;
import com.project.reddit.dto.post.PostDto;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.kafka.service.generic.model.PostCommentNotificationModel;
import com.project.reddit.mapper.AbstractPostMapper;
import com.project.reddit.mapper.AbstractUserMapper;
import com.project.reddit.repository.UserRepository;
import com.project.reddit.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageBrokerController<T>{


    private final UserRepository userRepository;

    private final CommentService commentService;

    private final SimpMessagingTemplate messagingTemplate;

    private final AbstractPostMapper postMapper;

    private final AbstractUserMapper userMapper;



    private T fromJsonToObject(String json, Class<?> toClass) {
        Gson gson = new Gson();

        if (toClass == List.class) {
            log.info("ListClass");
            //return gson.toJson(json);
            return (T) gson.toJson(json);
        }

        return (T) gson.fromJson(json, toClass);
        //return gson.fromJson(json, toClass);
    }


    //TODO
    /*
    * Change everything to kafka later
    * */

    /*
    * This method is used for sending PostDto object to all followers of one user
    * */
    @KafkaListener(topics = "FOLLOWER_NOTIFICATION", containerFactory="kafkaListenerContainerFactory")
    public void sendNotificationAfterPostIsSaved(@Payload PostDto postDto, Acknowledgment acknowledgment){

        var findUser = userRepository.findById(postDto.getPostedBy().getId()).orElseThrow(() -> new NotFoundException("User was not found"));

        //send notification to followers
        findUser.getFollowers().forEach(user -> {
            userMapper.userNotification2(postDto);
            messagingTemplate.convertAndSendToUser(user.getFrom().getUsername(), "/queue/notification", userMapper.userNotification2(postDto));
            //save users not
            user.getFrom().getNotifications().add(postMapper.postDtoToEntity(postDto));
            userRepository.save(user.getFrom());
        });

        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "POST_NOTIFICATION", containerFactory="kafkaListenerContainerFactory")
    public void afterPostIsSavedReturnListOfPostDtos(@Payload PostDto postDto ,Acknowledgment acknowledgment) {
        messagingTemplate.convertAndSend("/topic/post", postDto);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "USER_PROFILE_NOTIFICATION", containerFactory = "kafkaListenerContainerFactory")
    public void afterUserDeletesOrUpdatesPost(@Payload String body, Acknowledgment acknowledgment) {
       // messagingTemplate.convertAndSend("/topic/user", );
    }

    @KafkaListener(topics = "COMMENT_NOTIFICATION", containerFactory = "kafkaListenerContainerFactory")
    public void afterCommentIsUploaded(@Payload PostCommentNotificationModel postCommentNotificationModel, Acknowledgment acknowledgment) {
        messagingTemplate.convertAndSend("/topic/comment/" + postCommentNotificationModel.getPostId(), postCommentNotificationModel.getCommentDto());
    }




/*
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
    }*/

}
