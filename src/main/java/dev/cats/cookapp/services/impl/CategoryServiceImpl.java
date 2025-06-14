package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.response.recipe.CategoryResponse;
import dev.cats.cookapp.mappers.CategoryMapper;
import dev.cats.cookapp.repositories.CategoryRepository;
import dev.cats.cookapp.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    public Map<String, List<CategoryResponse>> findByNames(final List<String> names) {

        return names.stream()
                .flatMap(name ->
                        this.categoryRepository.findByNameIgnoreCaseContaining(name).stream())
                .map(this.categoryMapper::toResponse)
                .collect(Collectors.groupingBy(CategoryResponse::getType));
    }
}
