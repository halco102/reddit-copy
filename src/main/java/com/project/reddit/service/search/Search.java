package com.project.reddit.service.search;

import com.project.reddit.model.SearchTypes;

import java.util.Set;

public interface Search <T> {

    Set<T> search(String name);


}
