package com.project.reddit.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmbedableCommentLikeDislikeId implements Serializable {

    @Column(name = "users_id")
    private Long userId;

    @Column(name = "comments_id")
    private Long commentId;

}
