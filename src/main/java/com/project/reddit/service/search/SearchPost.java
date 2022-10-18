package com.project.reddit.service.search;

import com.project.reddit.dto.post.PostDto;
import com.project.reddit.mapper.AbstractPostMapper;
import com.project.reddit.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchPost implements Search<PostDto> {

    private final PostRepository postRepository;

    private final AbstractPostMapper postMapper;

    @Override
    public Set<PostDto> search(String name) {

        var searchResults = postRepository.searchPostsByName(name);

        if (searchResults.isEmpty()) {
            //empty list
            return new HashSet<>();
        }
        return searchResults.stream().map(m -> postMapper.toPostDto(m)).collect(Collectors.toSet());
    }

}
