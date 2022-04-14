package com.project.reddit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicUserInfo {

    private Long id;

    private String username;

    private String imageUrl;
}
