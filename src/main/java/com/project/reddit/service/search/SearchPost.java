package com.project.reddit.service.search;

import com.project.reddit.model.content.Post;
import com.project.reddit.model.user.User;
import com.project.reddit.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchPost implements Search<Post> {

    private final PostRepository postRepository;

    @Override
    public Set<Post> search(String name) {
        var searchResults = postRepository.searchPostsByName(name);

        return searchResults;
    }

}
