package com.project.reddit.model.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.reddit.model.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Entity
//@Table(name = "post_likes_dislikes")
public class PostLikeOrDislike {

    //@EmbeddedId
    private EmbedablePostLikeOrDislikeId embedablePostLikeOrDislikeId = new EmbedablePostLikeOrDislikeId();

/*    @MapsId("postId")
    @ManyToOne
    @JoinColumn(name = "posts_id", referencedColumnName = "id")
    @JsonIgnore*/
    private Post post;

/*    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @JsonIgnore*/
    private User user;

    //@Column(name = "is_like")
    private boolean likeOrDislike;

}
