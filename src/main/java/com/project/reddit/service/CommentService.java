package com.project.reddit.service;

import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.dto.comment.CommentRequest;
import com.project.reddit.dto.comment.EditCommentDto;
import com.project.reddit.dto.comment.LikeOrDislikeCommentRequest;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.mapper.CommentMapper;
import com.project.reddit.model.SearchTypes;
import com.project.reddit.model.message.Comment;
import com.project.reddit.model.message.CommentLikeDislike;
import com.project.reddit.model.message.EmbedableCommentLikeDislikeId;
import com.project.reddit.model.user.User;
import com.project.reddit.repository.CommentRepository;
import com.project.reddit.service.post.PostService;
import com.project.reddit.service.search.FilterUserContent;
import com.project.reddit.service.sorting.SortingCommentsInterface;
import com.project.reddit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final PostService postService;

    private final SortingCommentsInterface sortingCommentsInterface;

    private final FilterUserContent<Comment> filterUserContent;


    /*
    * This method is used for storing a new comment to database
    * and to return to FE a single DTO
    * */
    public CommentDto postComment(CommentRequest request) {

        // get the currently logged user
        var findUser = userService.getCurrentlyLoggedUser();

        //find the current post, if there isn't one it will throw an exception
        var findPost = this.postService.getPostEntityById(request.getPostId());

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

    /*
    * This method is used for liking or disliking a comment, or if a user liked a comment
    * if the user clicks like again the List should remove the liked comment.
    * */
    public CommentDto postLikeOrDislike(LikeOrDislikeCommentRequest request) {

        // find the requested comment
        var comment = this.commentRepository.findById(request.getCommentId());
        var user = userService.getCurrentlyLoggedUser();

        if (comment.isEmpty()) {
            throw new NotFoundException("The comment wiht id " + request.getCommentId() + " does not exist!");
        }

        if (user == null) {
            throw new NotFoundException("The user with id " + request.getUserId() + " does not exist");
        }

        // instantiate the object and set the values
        CommentLikeDislike temp = new CommentLikeDislike();
        temp.setEmbedableCommentLikeDislikeId(new EmbedableCommentLikeDislikeId(user.getId(), comment.get().getId()));
        temp.setLikeOrDislike(request.isLikeOrDislike());
        temp.setComment(comment.get());
        temp.setUser(user);


        if (!comment.get().getLikeDislikes().isEmpty()){
            var findComment = checkIfUserLikedDislikedComment(comment.get(), user);

            // if the list is empty just add the like or dislike to the list
            if (findComment.isEmpty()) {
                comment.get().getLikeDislikes().add(temp);
            }else {

                // if there is already an object remove it from list, this is used for if a user liked a comment and clicks like again
                if (findComment.get().isLikeOrDislike() == request.isLikeOrDislike()) {
                    comment.get().getLikeDislikes()
                            .removeIf(item -> item.getEmbedableCommentLikeDislikeId().getCommentId().equals(findComment.get().getComment().getId())
                                    && item.getEmbedableCommentLikeDislikeId().getUserId().equals(user.getId()));
                }else {
                    findComment.get().setLikeOrDislike(request.isLikeOrDislike());
                }


            }
        }else {
            comment.get().getLikeDislikes().add(temp);
        }



        var com = this.commentRepository.save(comment.get());

        var commentDto = commentMapper.toDto(com);
        return commentDto;
    }


    /*
    * Helper method for like or dislike comments
    * */
    private Optional<CommentLikeDislike> checkIfUserLikedDislikedComment(Comment comment, User user) {
        // find the comment object of liked or disliked comment
        var findComment = comment.getLikeDislikes()
                .stream()
                .filter(item -> item.getEmbedableCommentLikeDislikeId().getCommentId().equals(comment.getId()) &&
                        item.getEmbedableCommentLikeDislikeId().getUserId().equals(user.getId())).findAny();
        return findComment;
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
        return this.filterUserContent.filterUserContent(userId, SearchTypes.COMMENTS).stream().map(e -> commentMapper.toDto(e)).collect(Collectors.toList());
    }
}
