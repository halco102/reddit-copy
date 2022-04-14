package com.project.reddit.controller;

import com.project.reddit.dto.post.PostRequestDto;
import com.project.reddit.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;


    @PostMapping()
    public ResponseEntity<?> savePost(@RequestBody @Valid PostRequestDto requestDto) {
        return new ResponseEntity<>(this.postService.savePost(requestDto), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAllPostsForFrontPage() {
        return new ResponseEntity<>(this.postService.getAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return new ResponseEntity<>(this.postService.getPostDtoById(id), HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<?> getPostsByTitle(@RequestParam String title) {
        return new ResponseEntity<>(this.postService.getSimilarPostsByTitle(title) ,HttpStatus.OK);
    }
}
