package com.project.reddit.service.user.follow;

import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.exception.BadRequestException;
import com.project.reddit.mapper.AbstractUserMapper;
import com.project.reddit.repository.UserRepository;
import com.project.reddit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowService implements IFollow{

    private final UserRepository userRepository;

    private final AbstractUserMapper mapper;

    private final UserService userService;

    @Override
    public UserProfileDto followUser(Long id) {
        var currentlyLoggedUser = userService.getCurrentlyLoggedUser();
        var findUserToFollow = userService.getUserById(id);

        if (currentlyLoggedUser.getId() == findUserToFollow.getId()) {
            throw new BadRequestException("Cant follow yourself");
        }

        currentlyLoggedUser.getFollowing().add(findUserToFollow);

        var saveUser = userRepository.save(currentlyLoggedUser);
        return mapper.userProfileDto(saveUser);
    }

    @Override
    public UserProfileDto unfollowUser(Long id) {
        var currentlyLoggedUser = userService.getCurrentlyLoggedUser();

        currentlyLoggedUser.getFollowers().removeIf(e -> e.getId() == id);

        var saveUser = userRepository.save(currentlyLoggedUser);

        return mapper.userProfileDto(saveUser);
    }
}
