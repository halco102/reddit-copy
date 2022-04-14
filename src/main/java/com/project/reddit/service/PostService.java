package com.project.reddit.service;

import com.project.reddit.dto.post.PostDto;
import com.project.reddit.dto.post.PostRequestDto;
import com.project.reddit.dto.post.PostResponseDto;
import com.project.reddit.mapper.PostMapper;
import com.project.reddit.model.content.Post;
import com.project.reddit.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;

    public PostResponseDto savePost(PostRequestDto requestDto) {

        var user = userService.getUserById(requestDto.getUserId());
        Post post = postMapper.toEntity(requestDto);

        if (user == null) {
            log.info("No user shutdown");
            return null;
        }

        post.setAllowComments(requestDto.isAllowComment());
        post.setUser(user);

        var savedPost = this.postRepository.save(post);

        return postMapper.postResponse(savedPost);
    }

    public List<PostDto> getAllPosts() {
        var posts = this.postRepository.findAll();

        if (posts.isEmpty()) {
            log.info("No posts");
            return null;
        }

        var temp = posts.stream().map(e -> postMapper.toPostDto(e)).collect(Collectors.toList());
        return temp;
    }

    public Post findPostById(Long id) {
        return this.postRepository.findById(id).orElse(null);
    }

    public PostDto getPostDtoById(Long id) {
        var test1 =  this.postMapper.toPostDto(this.postRepository.findById(id).get());
        return test1;
    }

}
