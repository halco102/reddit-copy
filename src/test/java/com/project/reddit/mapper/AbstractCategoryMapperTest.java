package com.project.reddit.mapper;

import com.project.reddit.dto.category.CategoryDto;
import com.project.reddit.dto.category.CategoryRequestDto;
import com.project.reddit.model.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
class AbstractCategoryMapperTest {

    private CategoryDto categoryDto;

    private CategoryRequestDto categoryRequestDto;

    private Category category;


    @BeforeEach
    public void beforeEachSetup() {
        this.categoryDto = new CategoryDto(1L, "name", "icon");
        this.categoryRequestDto = new CategoryRequestDto("requestName", "requestIcon");
        this.category = new Category(2L, "name", "icon", new ArrayList<>());
    }

    @Test
    void toCategoryDtoTest() {
        var toDto = Mappers.getMapper(AbstractCategoryMapper.class).toCategoryDto(category);
        Assertions.assertEquals(toDto.getId(), category.getId());
        Assertions.assertEquals(toDto.getName(), category.getName());
        Assertions.assertEquals(toDto.getIconUrl(), category.getIconUrl());
    }

    @Test
    void fromCategoryRequestToEntityTest() {
        var fromRequestToEntity = Mappers.getMapper(AbstractCategoryMapper.class).fromCategoryRequestToEntity(categoryRequestDto);

        Assertions.assertEquals(fromRequestToEntity.getName(), categoryRequestDto.getName());
        Assertions.assertEquals(fromRequestToEntity.getIconUrl(), categoryRequestDto.getIconUrl());

        // null assestrions
        Assertions.assertNull(fromRequestToEntity.getPosts());
    }

    @Test
    void toCategoryEntityTest() {
        var toEntity = Mappers.getMapper(AbstractCategoryMapper.class).toCategoryEntity(categoryDto);

        Assertions.assertEquals(toEntity.getId(), categoryDto.getId());
        Assertions.assertEquals(toEntity.getName(), categoryDto.getName());
        Assertions.assertEquals(toEntity.getIconUrl(), categoryDto.getIconUrl());

        Assertions.assertNull(toEntity.getPosts());
    }
}