package com.project.reddit.dto.post;

import com.project.reddit.dto.category.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostForFrontPageDto {

    private Long id;

    private String title;

    private String text;

    private String imageUrl;

    private List<PostLikeOrDislikeDto> postLikeOrDislikeDtos;

    private Set<CategoryDto> categories;

}
