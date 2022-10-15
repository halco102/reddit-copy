package com.project.reddit.service.user.follow;

import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.mapper.AbstractFollowMapper;
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

    private final AbstractFollowMapper followMapper;

    private final UserService userService;

    @Override
    public UserProfileDto followUser(Long id) {
        var currentlyLoggedUser = userService.getCurrentlyLoggedUser();

        var findUserToFollow = userService.getUserById(id);

        var temp = followMapper.toFollwing(currentlyLoggedUser.getFollowing());
        temp.add(followMapper.fromUserToFollowDto(findUserToFollow));

        currentlyLoggedUser.setFollowing(followMapper.fromDtoToEntity(temp));
        var saveUser = userRepository.save(currentlyLoggedUser);
        return mapper.userProfileDto(saveUser);
    }

    @Override
    public UserProfileDto unfollowUser(Long id) {
        return null;
    }
}
