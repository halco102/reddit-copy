package com.project.reddit.controller;

import com.project.reddit.dto.comment.CommentRequest;
import com.project.reddit.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<?> postCommentOnPost(@RequestBody CommentRequest request) {
        return new ResponseEntity<>(commentService.postComment(request), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> testEndpoint() {
        return new ResponseEntity<>(this.commentService.getAllComments(), HttpStatus.OK);
    }

}
