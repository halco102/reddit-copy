package com.project.reddit.service.likedislike;

import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.dto.likeordislike.CommentLikeOrDislikeRequest;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.mapper.AbstractCommentMapper;
import com.project.reddit.model.likedislike.CommentLikeOrDislike;
import com.project.reddit.model.likedislike.EmbedableCommentLikeOrDislike;
import com.project.reddit.model.message.Comment;
import com.project.reddit.model.user.User;
import com.project.reddit.repository.CommentRepository;
import com.project.reddit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentLikeOrDislikeService implements ILikeOrDislike<CommentDto, CommentLikeOrDislikeRequest> {

    private final CommentRepository commentRepository;

    private final AbstractCommentMapper commentMapper;

    private final UserService userService;

    @Override
    public CommentDto likeOrDislike(CommentLikeOrDislikeRequest request) {
        // find the requested comment
        var comment = this.commentRepository.findById(request.getId());
        var user = userService.getCurrentlyLoggedUser();

        if (comment.isEmpty()) {
            throw new NotFoundException("The comment wiht id " + request.getId() + " does not exist!");
        }

        if (user == null) {
            throw new NotFoundException("The user with id " + request.getUserId() + " does not exist");
        }

        // instantiate the object and set the values
        CommentLikeOrDislike temp = new CommentLikeOrDislike();
        temp.setEmbedableCommentLikeDislikeId(new EmbedableCommentLikeOrDislike(user.getId(), comment.get().getId()));
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
    private Optional<CommentLikeOrDislike> checkIfUserLikedDislikedComment(Comment comment, User user) {
        // find the comment object of liked or disliked comment
        var findComment = comment.getLikeDislikes()
                .stream()
                .filter(item -> item.getEmbedableCommentLikeDislikeId().getCommentId().equals(comment.getId()) &&
                        item.getEmbedableCommentLikeDislikeId().getUserId().equals(user.getId())).findAny();
        return findComment;
    }
}
