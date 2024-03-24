package dev.cats.cookapp.services.category;

import dev.cats.cookapp.dto.response.RecipeCategoryResponse;

import java.util.List;

public interface CategoryService {
    List<RecipeCategoryResponse> getCategories();
}
