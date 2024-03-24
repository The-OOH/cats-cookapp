package dev.cats.cookapp.services.category;

import dev.cats.cookapp.dto.response.RecipeCategoryResponse;
import dev.cats.cookapp.mappers.RecipeCategoryMapper;
import dev.cats.cookapp.repositories.RecipeCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final RecipeCategoryRepository categoryRepository;
    private final RecipeCategoryMapper recipeCategoryMapper;

    public List<RecipeCategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(recipeCategoryMapper::toDto)
                .collect(Collectors.toList());
    }

}
