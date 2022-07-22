package com.project.reddit.service.search;

import com.project.reddit.constants.UserProfileSearchType;

import java.util.List;

public interface FilterUserContent<T> {

    List<T> filterUserContent(Long userId, UserProfileSearchType types);

}
