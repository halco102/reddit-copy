package com.project.reddit.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostNotificationDto {

    private Long id;

    private String title;

    private String imageUrl;

}
