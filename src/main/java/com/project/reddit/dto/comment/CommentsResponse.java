package com.project.reddit.dto.comment;

import com.project.reddit.dto.user.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsResponse {

    List<CommentDto> commentDtos;

    UserInfo userInfo;

}
