package com.project.reddit.dto.category;

import com.project.reddit.model.content.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    @NotBlank
    private String name;


}
