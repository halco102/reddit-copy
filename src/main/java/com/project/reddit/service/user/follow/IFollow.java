package com.project.reddit.service.user.follow;

import com.project.reddit.dto.user.UserProfileDto;

public interface IFollow {

    UserProfileDto followUser(Long id);

    UserProfileDto unfollowUser(Long id);

}
