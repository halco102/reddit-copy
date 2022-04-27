package com.project.reddit.dto.comment;

import com.project.reddit.dto.user.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    private String text;

    private UserInfo userInfo;

    private Long parentId;

    private List<LikedOrDislikedCommentsDto> likedOrDislikedComments = new ArrayList<>();
}
