package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dto.response.RecipeCategoryResponse;
import dev.cats.cookapp.services.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<RecipeCategoryResponse> getCategories(){
        return categoryService.getCategories();
    }

    @GetMapping("/important")
    public List<RecipeCategoryResponse> getImportantCategories(){
        return categoryService.getImportantCategories();
    }
}
