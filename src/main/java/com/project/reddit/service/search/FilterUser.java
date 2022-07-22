package com.project.reddit.service.search;

import com.project.reddit.exception.BadRequestException;
import com.project.reddit.exception.NotFoundException;
import com.project.reddit.constants.UserProfileSearchType;
import com.project.reddit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
/*
* This is used for searching user content in his user profile
* With this we can see what user posted or on what post he commented
* */
public class FilterUser<T> implements FilterUserContent<T>{

    private final UserRepository userRepository;

    @Override
    public List<T> filterUserContent(Long userId, UserProfileSearchType types) {

        var findUser = userRepository.findById(userId);

        if (findUser.isEmpty()) {
            throw new NotFoundException("User with ID " + userId + " was not found!");
        }

        if (types.equals(UserProfileSearchType.POST)) {
            return (List<T>) findUser.get().getPosts().stream().collect(Collectors.toList());

        } else if (types.equals(UserProfileSearchType.COMMENTS)) {
            return (List<T>) findUser.get().getComments().stream().collect(Collectors.toList());

        }else {
            throw new BadRequestException("Bad request, no enum for this usecase");
        }

    }

}
