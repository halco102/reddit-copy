package com.project.reddit.repository;

import com.project.reddit.model.content.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM posts as p inner join comments as c on p.id = c.posts_id where p.id = 1 order by parent_comment_id", nativeQuery = true)
    Post testQuery();

    @Query(value = "select * from posts as p inner join users as u on p.users_id = u.id inner join comments as c on p.id = c.posts_id where p.title LIKE :title%", nativeQuery = true)
    Set<Post> getPostsByTitle(@Param("title") String title);



}
