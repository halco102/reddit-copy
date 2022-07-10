package com.project.reddit.mapper;

import com.project.reddit.dto.category.CategoryDto;
import com.project.reddit.dto.category.CategoryRequestDto;
import com.project.reddit.model.category.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategoryEntity(CategoryDto categoryDto);

    Category fromCategoryRequestToEntity(CategoryRequestDto requestDto);

    CategoryDto toCategoryDto(Category category);

}
