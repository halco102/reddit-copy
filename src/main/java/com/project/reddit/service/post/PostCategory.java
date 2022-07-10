package com.project.reddit.service.post;

import com.project.reddit.dto.post.PostDto;

import java.util.List;

public interface PostCategory {

    List<PostDto> getAllPostByCategoryName(String categoryName);

}
