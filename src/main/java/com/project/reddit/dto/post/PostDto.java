package com.project.reddit.dto.post;

import com.project.reddit.dto.comment.CommentDto;
import com.project.reddit.model.message.Comment;
import com.project.reddit.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    private UserPostDto postedBy;

    private List<CommentDto> commentsDto = new ArrayList<>();

    private boolean allowComments;
}
