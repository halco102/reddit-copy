package com.project.reddit.model.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.reddit.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "likes_dislikes_comments")
//@Entity
public class CommentLikeDislike {

    //@EmbeddedId
    private EmbedableCommentLikeDislikeId embedableCommentLikeDislikeId = new EmbedableCommentLikeDislikeId();

/*    @ManyToOne
    @MapsId("userId")
    @JoinColumn(referencedColumnName = "id", name = "users_id")
    @JsonIgnore*/
    private User user;

/*    @ManyToOne
    @MapsId("commentId")
    @JoinColumn(referencedColumnName = "id", name = "comments_id")
    @JsonIgnore*/
    private Comment comment;

    //@Column(name = "is_like")
    private boolean likeOrDislike;


}
