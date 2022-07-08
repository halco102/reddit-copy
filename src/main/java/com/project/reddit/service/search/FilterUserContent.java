package com.project.reddit.service.search;

import com.project.reddit.model.SearchTypes;

import java.util.List;

public interface FilterUserContent<T> {

    List<T> filterUserContent(Long userId, SearchTypes types);

}
