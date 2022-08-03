package com.project.reddit.dto.user.follower;

import com.project.reddit.dto.user.UserAbstract;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FollowDto extends UserAbstract {

    public FollowDto(Long id, String username, String email, LocalDate createdAt, String imageUrl) {
        super(id, username, email, createdAt, imageUrl);
    }


}
