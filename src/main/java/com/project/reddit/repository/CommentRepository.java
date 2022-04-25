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


}
