package com.project.reddit.service.likedislike;

import com.project.reddit.constants.KafkaNotifications;
import com.project.reddit.dto.likeordislike.LikeOrDislikeRequest;
import com.project.reddit.dto.post.PostDto;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.kafka.service.generic.NotificationContext;
import com.project.reddit.kafka.service.generic.model.LikeDislikePostNotificationModel;
import com.project.reddit.mapper.AbstractPostMapper;
import com.project.reddit.model.content.Post;
import com.project.reddit.model.likedislike.EmbedablePostLikeOrDislike;
import com.project.reddit.model.likedislike.PostLikeOrDislike;
import com.project.reddit.model.user.User;
import com.project.reddit.repository.PostRepository;
import com.project.reddit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostLikeOrDislikeService implements ILikeOrDislike<PostDto, LikeOrDislikeRequest>{

    private final PostRepository postRepository;

    private final UserService userService;

    private final AbstractPostMapper postMapper;

    private final NotificationContext notificationContext;

    @Override
    @Transactional
    public PostDto likeOrDislike(LikeOrDislikeRequest request) {

        var getPostById = this.postRepository.findById(request.getId());
        var user = userService.getCurrentlyLoggedUser();

        if (getPostById.isEmpty()) {
            throw new NotFoundException("Post by id: " + request.getId() + " cannot be found");
        }


        PostLikeOrDislike postLikeOrDislike = new PostLikeOrDislike(user, request.isLikeOrDislike(),
                new EmbedablePostLikeOrDislike(user.getId(), getPostById.get().getId()),
                getPostById.get());


        if (!getPostById.get().getPostLikeOrDislikes().isEmpty()){
            var findComment = checkIfUserLikedPost(getPostById.get(), user);

            if (findComment.isEmpty()) {
                getPostById.get().getPostLikeOrDislikes().add(postLikeOrDislike);
            }else {
                // if the request is same set likeDislike to null
                if (findComment.get().isLikeOrDislike() == request.isLikeOrDislike()) {
                    // same like request as in the db, remove it from list
                    getPostById.get().getPostLikeOrDislikes()
                            .removeIf(i -> i.getEmbedablePostLikeOrDislikeId().getPostId().equals(findComment.get().getEmbedablePostLikeOrDislikeId().getPostId())
                                    && i.getEmbedablePostLikeOrDislikeId().getUserId().equals(user.getId()));
                }else {
                    // there is no same request as in db add it to the list
                    findComment.get().setLikeOrDislike(request.isLikeOrDislike());
                }
            }

        }else {
            // add if its empty
            getPostById.get().getPostLikeOrDislikes().add(postLikeOrDislike);
        }

        var savePost = this.postRepository.save(getPostById.get());


        var postDto = postMapper.toPostDto(savePost);

        notificationContext.sendMessageToKafka(KafkaNotifications.LIKE_OR_DISLIKE_POST_NOTIFICATION, new LikeDislikePostNotificationModel(
               postDto, "LIKE_OR_DISLIKE_POST"), KafkaNotifications.LIKE_OR_DISLIKE_POST_NOTIFICATION.name());

        return postDto;
    }

    /*
     * Helper method for save like or dislike on posts
     * Checks if the user already liked or disliked the post
     * */
    private Optional<PostLikeOrDislike> checkIfUserLikedPost(Post post, User user) {
        var findComment = post.getPostLikeOrDislikes()
                .stream()
                .filter(item -> item.getEmbedablePostLikeOrDislikeId().getPostId().equals(post.getId()) &&
                        item.getEmbedablePostLikeOrDislikeId().getUserId().equals(user.getId())).findAny();
        return findComment;
    }

}
