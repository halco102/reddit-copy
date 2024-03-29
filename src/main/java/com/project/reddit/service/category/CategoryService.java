package com.project.reddit.service.category;

import com.project.reddit.dto.category.CategoryDto;
import com.project.reddit.dto.category.CategoryRequestDto;
import com.project.reddit.mapper.AbstractCategoryMapper;
import com.project.reddit.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryService implements CategoryInterface{

    private final CategoryRepository categoryRepository;

    private final AbstractCategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(m -> categoryMapper.toCategoryDto(m)).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryByName(String name) {
        return null;
    }

    @Override
    public CategoryDto saveCategory(CategoryRequestDto requestDto) {
        var category = categoryMapper.fromCategoryRequestToEntity(requestDto);
        var savedObj = this.categoryRepository.save(category);

        return categoryMapper.toCategoryDto(savedObj);
    }

}
