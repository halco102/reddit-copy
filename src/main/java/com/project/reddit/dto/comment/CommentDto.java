package com.project.reddit.dto.comment;

import com.project.reddit.dto.user.BasicUserInfo;
import com.project.reddit.dto.user.UserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    private String text;

    private BasicUserInfo userInfo;

    private Long parentId;

}
