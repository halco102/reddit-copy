package com.project.reddit.service.search;

import com.project.reddit.model.SearchTypes;
import com.project.reddit.model.user.User;

import java.util.List;
import java.util.Set;

public interface FilterUserContent<T> {

    List<T> filterUserContent(Long userId, SearchTypes types);

}
