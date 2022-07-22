package com.project.reddit.model.likedislike;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.reddit.model.message.Comment;
import com.project.reddit.model.message.EmbedableCommentLikeDislikeId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes_dislikes_comments")
@Entity
public class CommentLikeOrDislike extends LikeOrDislike{

    @EmbeddedId
    private EmbedableCommentLikeOrDislike embedableCommentLikeDislikeId = new EmbedableCommentLikeOrDislike();

    @ManyToOne
    @MapsId("commentId")
    @JoinColumn(referencedColumnName = "id", name = "comments_id")
    @JsonIgnore
    private Comment comment;

}
