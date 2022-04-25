package com.project.reddit.dto.comment;

import com.project.reddit.model.message.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikedOrDislikedComments {


    private CommentDto commentDto;

    private boolean likeOrDislike;

}
