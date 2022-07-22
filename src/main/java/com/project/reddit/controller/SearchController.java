package com.project.reddit.controller;

import com.project.reddit.constants.SearchType;
import com.project.reddit.service.search.SearchContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/search")
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchContext searchContext;

    @GetMapping("/{content}")
    public ResponseEntity<?> searchPostOrUser(@PathVariable String content, @RequestParam SearchType searchType) {
        return new ResponseEntity<>(this.searchContext.getSearchResult(searchType, content), HttpStatus.OK);
    }

}
