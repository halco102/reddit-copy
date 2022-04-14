package com.project.reddit.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotNull
    @NotBlank
    private String text;

    @NotNull
    private Long postId;

    @NotNull
    private Long userId;

    private Long parentId;
}
