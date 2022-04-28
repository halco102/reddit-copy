package com.project.reddit.model.content;

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
public class EmbedablePostLikeOrDislikeId implements Serializable {

    @Column(name = "posts_id")
    private Long postId;

    @Column(name = "users_id")
    private Long userId;

}
