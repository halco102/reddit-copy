package com.project.reddit.dto.post;

import com.project.reddit.dto.user.BasicUserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostForFrontPageDto {

    private Long id;

    private String title;

    private String imageUrl;

    private BasicUserInfo basicUserInfo;
}
