package com.project.reddit.controller;

import com.project.reddit.dto.category.CategoryRequestDto;
import com.project.reddit.service.category.CategoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryInterface categoryInterface;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return new ResponseEntity<>(categoryInterface.getAllCategories(), HttpStatus.OK);
    }

    /*
    * Categories should have its own admin panel
    * but for now its gonna be all added by me
    * and just get will have its use
    * */
    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody CategoryRequestDto req) {
        return new ResponseEntity<>(categoryInterface.saveCategory(req), HttpStatus.OK);
    }

}
