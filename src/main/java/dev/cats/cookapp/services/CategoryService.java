package dev.cats.cookapp.services;

import dev.cats.cookapp.dtos.response.recipe.CategoryResponse;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    Map<String, List<CategoryResponse>> findByNames(List<String> names);
}
