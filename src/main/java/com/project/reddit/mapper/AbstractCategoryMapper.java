package com.project.reddit.mapper;

import com.project.reddit.dto.category.CategoryDto;
import com.project.reddit.dto.category.CategoryRequestDto;
import com.project.reddit.model.category.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AbstractCategoryMapper {

    public abstract Category toCategoryEntity(CategoryDto categoryDto);

    public abstract Category fromCategoryRequestToEntity(CategoryRequestDto requestDto);

    public abstract CategoryDto toCategoryDto(Category category);

}
