package com.project.reddit.service.search;

import com.project.reddit.constants.SearchType;
import com.project.reddit.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class SearchContext {

    private final Map<SearchType, Search> map = new HashMap<>();

    public SearchContext(List<Search> searches) {
        for (Search s: searches
             ) {
            if (s instanceof SearchPost) {
                map.put(SearchType.SEARCH_CONTENT, s);
            }else if (s instanceof SearchUser){
                map.put(SearchType.SEARCH_USER, s);
            }else {
                throw new NotFoundException("Not");
            }
        }
    }

    public Set getSearchResult(SearchType searchType, String content) {
        return map.get(searchType).search(content);
    }

}
