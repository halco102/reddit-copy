package com.project.reddit.dto.post;

import com.project.reddit.dto.user.UserInfo;
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
    private UserInfo postedBy;

}
