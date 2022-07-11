package com.project.reddit.dto.post;

import com.project.reddit.dto.category.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @NotBlank
    @NotNull
    private String title;

    private String text;

    @NotBlank
    private String imageUrl;

    private boolean allowComments;

    @NotNull
    private Set<CategoryDto> categories;

}
