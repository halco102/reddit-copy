package com.project.reddit.model.likedislike;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.reddit.model.content.Post;
import com.project.reddit.model.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post_likes_dislikes")
@EqualsAndHashCode
public class PostLikeOrDislike extends LikeOrDislike{


    @EmbeddedId
    private EmbedablePostLikeOrDislike embedablePostLikeOrDislikeId = new EmbedablePostLikeOrDislike();

    @MapsId("postId")
    @ManyToOne
    @JoinColumn(name = "posts_id", referencedColumnName = "id")
    @JsonIgnore
    private Post post;

    public PostLikeOrDislike(User user, boolean likeOrDislike, EmbedablePostLikeOrDislike embedablePostLikeOrDislikeId, Post post) {
        super(user, likeOrDislike);
        this.embedablePostLikeOrDislikeId = embedablePostLikeOrDislikeId;
        this.post = post;
    }
}
