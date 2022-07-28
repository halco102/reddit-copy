package com.project.reddit.model.likedislike;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmbedableCommentLikeOrDislike implements Serializable {

    @Column(name = "users_id")
    private Long userId;

    @Column(name = "comments_id")
    private Long commentId;

}
