package com.project.reddit.service.category;

import com.project.reddit.dto.category.CategoryDto;
import com.project.reddit.dto.category.CategoryRequestDto;

import java.util.List;

public interface CategoryInterface {

    List<CategoryDto> getAllCategories();

    CategoryDto getCategoryByName(String name);

    CategoryDto saveCategory(CategoryRequestDto requestDto);

}
