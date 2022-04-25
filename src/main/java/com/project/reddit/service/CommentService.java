package com.project.reddit.service;

import com.project.reddit.dto.comment.*;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.mapper.CommentMapper;
import com.project.reddit.model.message.Comment;
import com.project.reddit.model.message.CommentLikeDislike;
import com.project.reddit.model.message.EmbedableCommentLikeDislikeId;
import com.project.reddit.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final PostService postService;

    public CommentDto postComment(CommentRequest request) {
        var findUser = this.userService.getUserById(request.getUserId());
        var findPost = this.postService.findPostById(request.getPostId());

        Comment comment = commentMapper.toEntity(request);
        comment.setCreatedAt(LocalDate.now());
        comment.setPost(findPost);
        comment.setUser(findUser);

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

    public List<CommentDto> getAllComments() {
        var getAllCommentsFromDb = commentRepository.findAll();

        if(getAllCommentsFromDb == null) {
            throw new NotFoundException("The cooments are null");
        }

        return getAllCommentsFromDb.stream().map(e -> this.commentMapper.toDto(e)).collect(Collectors.toList());
    }

    public CommentLikeDislike postLikeOrDislike(LikeOrDislikeCommentRequest request) {
        var comment = this.commentRepository.findById(request.getCommentId());
        var user = this.userService.getUserById(request.getUserId());
        List<CommentLikeDislike> list = new ArrayList<>();

        if (comment.isEmpty()) {
            throw new NotFoundException("The comment wiht id " + request.getCommentId() + " does not exist!");
        }

        if (user == null) {
            throw new NotFoundException("The user with id " + request.getUserId() + " does not exist");
        }
        CommentLikeDislike temp = new CommentLikeDislike();
        temp.setEmbedableCommentLikeDislikeId(new EmbedableCommentLikeDislikeId(user.getId(), comment.get().getId()));
        temp.setLikeOrDislike(request.isLikeOrDislike());
        temp.setComment(comment.get());
        temp.setUser(user);
        list.add(temp);

        comment.get().setLikeDislikes(list);

        var com = this.commentRepository.save(comment.get());
        return temp;
    }

    public List<CommentDto> getAllCommentsByPostId(Long id) {
        var comments = this.commentRepository.getAllCommentsByPostId(id);

        if(comments == null) {
            throw new NotFoundException("Comments are null");
        }

        return comments.stream().map(e -> commentMapper.toDto(e)).collect(Collectors.toList());
    }

}
