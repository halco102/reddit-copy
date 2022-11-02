package com.project.reddit.controller;

import com.project.reddit.dto.post.PostDto;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.kafka.service.generic.model.LikeDislikeCommentNotificationModel;
import com.project.reddit.kafka.service.generic.model.LikeDislikePostNotificationModel;
import com.project.reddit.kafka.service.generic.model.PostCommentNotificationModel;
import com.project.reddit.mapper.AbstractUserMapper;
import com.project.reddit.repository.UserRepository;
import com.project.reddit.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageBrokerController{


    private final UserRepository userRepository;

    private final SimpMessagingTemplate messagingTemplate;

    private final AbstractUserMapper userMapper;

    private final PostService postService;


    /*
    * This method is used for sending PostDto object to all followers of one user
    * */
    @KafkaListener(topics = "FOLLOWER_NOTIFICATION", containerFactory="kafkaListenerContainerFactory")
    public void sendNotificationAfterPostIsSaved(@Payload PostDto postDto, Acknowledgment acknowledgment){

        var findUser = userRepository.findById(postDto.getPostedBy().getId()).orElseThrow(() -> new NotFoundException("User was not found"));

        findUser.getFollowers().forEach(user -> {

            messagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/notification", userMapper.userNotificationFromPostDto(postDto));

            user.getNotifications().add(postService.getPostEntityById(postDto.getId()));

            userRepository.save(user);
        });
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "POST_NOTIFICATION", containerFactory="kafkaListenerContainerFactory")
    public void afterPostIsSavedReturnListOfPostDtos(@Payload PostDto postDto ,Acknowledgment acknowledgment) {
        messagingTemplate.convertAndSend("/topic/post", postDto);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "COMMENT_NOTIFICATION", containerFactory = "kafkaListenerContainerFactory")
    public void afterCommentIsUploaded(@Payload PostCommentNotificationModel postCommentNotificationModel, Acknowledgment acknowledgment) {
        messagingTemplate.convertAndSend("/topic/comment/" + postCommentNotificationModel.getPostId(), postCommentNotificationModel.getCommentDto());
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "DELETE_COMMENT_NOTIFICATION", containerFactory = "kafkaListenerContainerFactory")
    public void sendMsgAfterCommentIsDeleted(@Payload Long postId, Acknowledgment acknowledgment){
        messagingTemplate.convertAndSend("/topic/comment/" + postId, "COMMENT_DELETED");
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "LIKE_OR_DISLIKE_POST_NOTIFICATION", containerFactory = "kafkaListenerContainerFactory")
    public void triggerEventWhenUserLikesOrDislikesPost(@Payload LikeDislikePostNotificationModel body, Acknowledgment acknowledgment) {
        messagingTemplate.convertAndSend("/topic/post/like-dislike", body);
        acknowledgment.acknowledge();
    }


    @KafkaListener(topics = "LIKE_OR_DISLIKE_COMMENT_NOTIFICATION", containerFactory = "kafkaListenerContainerFactory")
    public void triggerEventWhenUserLikesOrDislikesComment(@Payload LikeDislikeCommentNotificationModel body, Acknowledgment acknowledgment) {
        messagingTemplate.convertAndSend("/topic/comment/" + body.getPostId() + "/like-dislike", body);
        acknowledgment.acknowledge();
    }


}
