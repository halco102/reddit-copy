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
public class UpdatePostDto {

    @NotBlank
    @NotNull
    private String title;

    private String text;

    private boolean allowComments;

    private Set<CategoryDto> categories;

}
