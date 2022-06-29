package com.project.reddit.service.search;

import com.project.reddit.model.user.User;
import com.project.reddit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchUser implements Search<User> {

    private final UserRepository userRepository;

    @Override
    public Set<User> search(String name) {
        var serchResults = userRepository.searchPostsByName(name);

        return serchResults;
    }

}
