package com.project.reddit.service;

import com.project.reddit.constants.KafkaNotifications;
import com.project.reddit.constants.UserProfileSearchType;
import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.dto.comment.CommentRequest;
import com.project.reddit.dto.comment.EditCommentDto;
import com.project.reddit.dto.likeordislike.CommentLikeOrDislikeRequest;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.kafka.service.generic.NotificationContext;
import com.project.reddit.kafka.service.generic.model.PostCommentNotificationModel;
import com.project.reddit.mapper.AbstractCommentMapper;
import com.project.reddit.model.content.Post;
import com.project.reddit.model.message.Comment;
import com.project.reddit.repository.CommentRepository;
import com.project.reddit.service.comment.DeleteComment;
import com.project.reddit.service.likedislike.CommentLikeOrDislikeService;
import com.project.reddit.service.likedislike.SimpleLikeOrDislikeFactory;
import com.project.reddit.service.post.PostInterface;
import com.project.reddit.service.search.FilterUserContent;
import com.project.reddit.service.sorting.SortingCommentsInterface;
import com.project.reddit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService implements DeleteComment {

    private final CommentRepository commentRepository;
    private final AbstractCommentMapper commentMapper;
    private final UserService userService;

    private final PostInterface postInterface;

    private final SortingCommentsInterface sortingCommentsInterface;

    private final FilterUserContent<Comment> filterUserContent;

    private final SimpleLikeOrDislikeFactory factory;

    private final NotificationContext notificationContext;


    /*
    * This method is used for storing a new comment to database
    * and to return to FE a single DTO
    * */
    public CommentDto postComment(CommentRequest request) {

        // get the currently logged user
        var findUser = userService.getCurrentlyLoggedUser();

        //find the current post, if there isn't one it will throw an exception
        var findPost = this.postInterface.getPostEntityById(request.getPostId());

        // map the request to entity and add set objects
        Comment comment = commentMapper.toEntity(request);
        comment.setCreatedAt(LocalDate.now());
        comment.setPost(findPost);
        comment.setUser(findUser);

        // check if requested comment has a parentId if it has check if the base comment exists in db
        if (request.getParentId() != null && request.getParentId() != 0) {
            var checkForParentId = this.commentRepository.findById(request.getParentId());
            if (checkForParentId.isEmpty()) {
                log.info("Parent comment does not exist");
                throw new NotFoundException("Parent comment id cannot be found");
            }

            var saveComment = this.commentRepository.save(comment);
            return this.commentMapper.toDto(saveComment);
        }


        var saveComment = this.commentRepository.save(comment);

        notificationContext.sendMessageToKafka(KafkaNotifications.COMMENT_NOTIFICATION, new PostCommentNotificationModel(
                commentMapper.toDto(comment),
                request.getPostId()
        ), KafkaNotifications.COMMENT_NOTIFICATION.name());

        return this.commentMapper.toDto(saveComment);
    }

    public CommentDto editCommentById(EditCommentDto editCommentDto) {
        var findOldComment = this.commentRepository.findById(editCommentDto.getId());
        if (findOldComment.isEmpty()) {
            log.info("That comment does not exist");
            return null;
        }

        findOldComment.get().setEdited(LocalDate.now());
        findOldComment.get().setText(editCommentDto.getText());

        var saveComment = this.commentRepository.save(findOldComment.get());
        return this.commentMapper.toDto(saveComment);
    }

    /*
    * Find all comments that are stored in db
    * if the List is null throw an exception
    * */
    public List<CommentDto> getAllComments() {
        var getAllCommentsFromDb = commentRepository.findAll();

        if(getAllCommentsFromDb == null) {
            throw new NotFoundException("The cooments are null");
        }

        return getAllCommentsFromDb.stream().map(e -> this.commentMapper.toDto(e)).collect(Collectors.toList());
    }

    public CommentDto postLikeOrDislike(CommentLikeOrDislikeRequest request) {
       return (CommentDto) factory.getImplementation(CommentLikeOrDislikeService.class).likeOrDislike(request);
    }

    /*
    * This method is used for getting all comments from a single post
    * if the list is null it throws an exception
    * */
    public List<CommentDto> getAllCommentsByPostId(Long id) {
        var comments = this.commentRepository.getAllCommentsByPostId(id);

        if(comments == null) {
            throw new NotFoundException("Comments are null");
        }

        var commentDtos = comments.stream().map(e -> commentMapper.toDto(e)).collect(Collectors.toList());

        return sortingCommentsInterface.sortingComments(commentDtos);
    }

    /*
    * This method is used for deleting a comment by id
    * Only user has right to delete his own comment (needs implementation)
    * If the main (base) comment is deleted all other comments that were pointing to base
    * comment will be deleted
    * */
    public void deleteCommentById(Long id) {
        var comments = this.commentRepository.getAllCommentsAndReplies(id);
        var user = userService.getCurrentlyLoggedUser();

        if (comments.isEmpty() || comments == null) {
            throw new NotFoundException("Comment deos not exist");
        }


        //delete also other comments that replied to the main comment


        comments.forEach(item -> this.commentRepository.delete(item));

    }


    public List<CommentDto> filterUserComments(Long userId) {
        return this.filterUserContent.filterUserContent(userId, UserProfileSearchType.COMMENTS).stream().map(e -> commentMapper.toDto(e)).collect(Collectors.toList());
    }

    @Override
    public void deleteAllCommentsByPostId(Post post) {
        var getComments = post.getComments();
        getComments.stream().forEach(item -> deleteCommentById(item.getId()));
    }

}
