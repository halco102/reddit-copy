package com.project.reddit.service.post;

import com.project.reddit.dto.post.PostDto;
import com.project.reddit.dto.post.PostRequestDto;
import com.project.reddit.dto.post.UpdatePostDto;
import com.project.reddit.model.content.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface PostInterface {

    PostDto savePost(PostRequestDto requestDto, MultipartFile file);
    void deletePostById(Long id);
    List<PostDto> getAllPosts();
    PostDto getPostDtoById(Long id);
    List<PostDto> getSimilarPostsByTitle(String title);

    Post getPostEntityById(Long id);

    PostDto updatePostById(Long id, UpdatePostDto request);
}
