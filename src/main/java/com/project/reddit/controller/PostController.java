package com.project.reddit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.reddit.dto.post.PostLikeOrDislikeRequest;
import com.project.reddit.dto.post.PostRequestDto;
import com.project.reddit.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@RestController
@Slf4j
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PostMapping(value = "", consumes = {  MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> savePost(@RequestPart("requestDto") String requestDto,
                                      @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PostRequestDto postRequestDto = objectMapper.readValue(requestDto, PostRequestDto.class);


        return new ResponseEntity<>(this.postService.savePost(postRequestDto, multipartFile), HttpStatus.OK);
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PostMapping("/like-dislike")
    public ResponseEntity<?> postLikeOrDislike(@RequestBody PostLikeOrDislikeRequest request){
        return new ResponseEntity<>(this.postService.saveLikeOrDislikeForPost(request), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable Long id) {
        this.postService.deletePostById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
