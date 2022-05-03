package com.project.reddit.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @NotBlank
    @NotNull
    private String title;

    private String text;

    private String imageUrl;


/*    @NotNull
    private Long userId;*/

    private boolean allowComments;

}
