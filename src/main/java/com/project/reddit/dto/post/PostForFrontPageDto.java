package com.project.reddit.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostForFrontPageDto {

    private Long id;

    private String title;

    private String text;

    private String imageUrl;

}
