package com.project.reddit.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPostDto {

    private Long id;

    private String username;

    private String imageUrl;

}
