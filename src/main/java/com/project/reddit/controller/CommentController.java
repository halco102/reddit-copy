package com.project.reddit.controller;

import com.project.reddit.dto.comment.CommentRequest;
import com.project.reddit.dto.comment.LikeOrDislikeCommentRequest;
import com.project.reddit.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping()
    public ResponseEntity<?> postCommentOnPost(@RequestBody CommentRequest request) {
        return new ResponseEntity<>(commentService.postComment(request), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> testEndpoint() {
        return new ResponseEntity<>(this.commentService.getAllComments(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/like-dislike")
    public ResponseEntity<?> postLikeOrDislike(@RequestBody LikeOrDislikeCommentRequest request) {
        return new ResponseEntity<>(this.commentService.postLikeOrDislike(request), HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<?> getAllCommentsByPostId(@PathVariable Long id){
        return new ResponseEntity<>(this.commentService.getAllCommentsByPostId(id), HttpStatus.OK);
    }

}
