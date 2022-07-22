package com.project.reddit.model.likedislike;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.reddit.model.user.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class LikeOrDislike {


    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @Column(name = "is_like")
    private boolean likeOrDislike;



}
