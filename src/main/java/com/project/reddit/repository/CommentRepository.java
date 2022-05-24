package com.project.reddit.repository;

import com.project.reddit.model.message.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "Select * from comments where posts_id = :id", nativeQuery = true)
    List<Comment> getAllCommentsByPostId(@Param("id") Long id);

    @Query(value = "select * from comments where id = :id or parent_comment_id = :id", nativeQuery = true)
    List<Comment> getAllCommentsAndReplies(@Param("id") Long id);

    @Query(value = "with recursive sorting as (" +
            "    select c.*, id as root_id" +
            "    from comments as c where c.posts_id = :postId and c.parent_comment_id is null" +
            "                       union all" +
            "    select d.*, s.root_id" +
            "    from comments as d inner join sorting as s" +
            "    on s.id = d.parent_comment_id" +
            ") select id, text, created_at, edited, posts_id, users_id, parent_comment_id from sorting order by  root_id, id, created_at", nativeQuery = true)
    public List<Comment> sortedComments(@Param("postId") Long postId);

}
