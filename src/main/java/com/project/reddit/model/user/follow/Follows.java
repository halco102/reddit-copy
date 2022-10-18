package com.project.reddit.model.user.follow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.reddit.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_follow")
public class Follows {

    @EmbeddedId
    private EmbedableFollow embedableFollow = new EmbedableFollow();

    @MapsId("userId")
    @JoinColumn(name = "person_id")
    @ManyToOne
    //@JsonIgnoreProperties("following")
    private User from;

    @MapsId("followId")
    @JoinColumn(name = "follow_id")
    //@JsonIgnoreProperties("followers")
    @ManyToOne
    private User to;

}
