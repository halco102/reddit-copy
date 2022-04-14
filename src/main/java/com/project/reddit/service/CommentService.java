package com.project.reddit.service;

import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.dto.comment.CommentRequest;
import com.project.reddit.dto.comment.EditCommentDto;
import com.project.reddit.mapper.CommentMapper;
import com.project.reddit.model.message.Comment;
import com.project.reddit.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        if (findUser == null) {
            log.info("User cannot be found");
            return null;
        }else if (findPost == null) {
            log.info("Post cannt be found");
            return null;
        }

        Comment comment = commentMapper.toEntity(request);

        comment.setCreatedAt(LocalDate.now());
        comment.setPost(findPost);
        comment.setUser(findUser);

        if (request.getParentId() != null && request.getParentId() != 0) {
            var checkForParentId = this.commentRepository.findById(request.getParentId());
            if (checkForParentId.isEmpty()) {
                log.info("Parent comment does not exist");
                return null;
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
        return this.commentRepository.findAll().stream().map(e -> this.commentMapper.toDto(e)).collect(Collectors.toList());
    }

}
