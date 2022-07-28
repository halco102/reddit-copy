package com.project.reddit.service.category;

import com.project.reddit.dto.category.CategoryDto;
import com.project.reddit.dto.category.CategoryRequestDto;
import com.project.reddit.mapper.CategoryMapper;
import com.project.reddit.mapper.CategoryMapperImpl;
import com.project.reddit.model.category.Category;
import com.project.reddit.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CategoryMapper categoryMapper;

    @InjectMocks
    CategoryService categoryService;

    private List<Category> categories = new ArrayList<>();

    @BeforeEach
    void setup() {

        categories.add(new Category(1L, "name1", "icon", new ArrayList<>()));
        categories.add(new Category(2L, "name2", "icon", new ArrayList<>()));
        categories.add(new Category(3L, "name3", "icon", new ArrayList<>()));
        categories.add(new Category(4L, "name4", "icon", new ArrayList<>()));
        categories.add(new Category(5L, "name5", "icon", new ArrayList<>()));
    }

    @Test
    void testGetAllCategories() {

        when(categoryRepository.findAll()).thenReturn(categories);

        when(categoryMapper.toCategoryDto(Mockito.any())).thenReturn(new CategoryDto(categories.get(0).getId(), categories.get(0).getName(), categories.get(0).getIconUrl()));

        var getAll = categoryService.getAllCategories();


        assertNotNull(getAll);

        assertEquals(categories.size(), getAll.size());

        assertEquals(categories.get(0).getId(), getAll.get(0).getId());


    }


    @Test
    void saveCategory() {

        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("newName", "newIcon");

        Category category = new Category(null, categoryRequestDto.getName(), categoryRequestDto.getIconUrl(), new ArrayList<>());
        CategoryDto categoryDto = new CategoryDto(1L, "newName", "newIcon");

        when(categoryMapper.fromCategoryRequestToEntity(Mockito.any())).thenReturn(category);

        category.setId(1L);

        when(categoryRepository.save(Mockito.any())).thenReturn(category);

        when(categoryMapper.toCategoryDto(Mockito.any())).thenReturn(categoryDto);

        var save = categoryService.saveCategory(categoryRequestDto);

        assertNotNull(save);

        assertEquals(categoryDto.getId(), save.getId());

        assertEquals(categoryDto.getName(), save.getName());

    }
}