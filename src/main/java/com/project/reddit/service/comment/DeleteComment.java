package com.project.reddit.service.comment;

import com.project.reddit.model.content.Post;

public interface DeleteComment {

    void deleteAllCommentsByPostId(Post post);

}
