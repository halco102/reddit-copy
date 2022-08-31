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


    /*
    * sort posts by number of likes
    * if there are no likes or dislikes on posts (it would be null) change it to 0
    * */
    @Query(value = "select posts.*, c.*, u.*, coalesce(sum(CASE WHEN pld.is_like = :condition THEN 1 ELSE 0 END)) as cnt from posts" +
            "    left join post_likes_dislikes pld on posts.id = pld.posts_id" +
            "    left outer join comments c on posts.id = c.posts_id" +
            "    left join users u on posts.users_id = u.id" +
            "    group by posts.id, c.id, u.id order by cnt desc", nativeQuery = true)
    Set<Post> sortPostByLikesOrDislikes(@Param("condition") boolean condition);


    /*
    * Search posts by title
    * */
    @Query(value = "Select * from posts where title like :title% order by created_at desc", nativeQuery = true)
    Set<Post> searchPostsByName(@Param("title") String title);

    @Query(value = "SELECT p.*, c.* from post_categories inner join categories c on c.id = post_categories.categories_id inner join posts p on p.id = post_categories.posts_id where c.name = :categoryName", nativeQuery = true)
    List<Post> getAllPostByCategoryName(@Param("categoryName") String categoryName);
}
