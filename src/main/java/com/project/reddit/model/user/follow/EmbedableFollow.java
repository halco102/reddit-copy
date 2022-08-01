package com.project.reddit.model.user.follow;

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
public class EmbedableFollow implements Serializable {

    @Column(name = "person_id")
    private Long userId;

    @Column(name = "follow_id")
    private Long followId;


}
