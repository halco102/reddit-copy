package com.project.reddit.dto.post;

import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;

    @NotBlank
    @NotNull
    private String title;

    private String text;

    private String imageUrl;

    @NotNull
    @NotBlank
    private UserPostDto postedBy;

}
