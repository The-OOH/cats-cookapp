package dev.cats.cookapp.services.recipe;

import dev.cats.cookapp.dto.response.RecipeResponse;
import dev.cats.cookapp.mappers.RecipeMapper;
import dev.cats.cookapp.models.*;
import dev.cats.cookapp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    @Override
    public Page<RecipeResponse> getRecipes(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Recipe> pageResponse = recipeRepository.findAll(pageRequest);
        return pageResponse.map(recipeMapper::toDto);
    }
}
