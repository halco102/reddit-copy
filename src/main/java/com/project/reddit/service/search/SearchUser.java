package com.project.reddit.service.search;

import com.project.reddit.dto.user.UserProfileDto;
import com.project.reddit.mapper.UserMapper;
import com.project.reddit.model.user.User;
import com.project.reddit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchUser implements Search<UserProfileDto> {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public Set<UserProfileDto> search(String name) {
        var searchResults = userRepository.searchPostsByName(name);

        if (searchResults.isEmpty()) {
            //empty list
            return new HashSet<>();
        }

        return searchResults.stream()
                .map(m -> userMapper.userProfileDto(m)).collect(Collectors.toSet());
    }

}
