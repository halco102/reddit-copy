package com.project.reddit.dto.post;

import com.project.reddit.dto.category.CategoryDto;
import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.dto.user.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;

    @NotBlank
    @NotNull
    private String title;

    private String text;

    private String imageUrl;

    @NotNull
    @NotBlank
    private UserInfo postedBy;

    private List<CommentDto> commentsDto = new ArrayList<>();

    private boolean allowComments;

    private List<PostLikeOrDislikeDto> postLikeOrDislikeDtos = new ArrayList<>();

    private Set<CategoryDto> categories;

}
