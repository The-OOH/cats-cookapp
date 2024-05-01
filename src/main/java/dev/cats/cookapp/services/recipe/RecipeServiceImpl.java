package dev.cats.cookapp.services.recipe;

import dev.cats.cookapp.dto.response.RecipeListResponse;
import dev.cats.cookapp.dto.response.RecipeResponse;
import dev.cats.cookapp.mappers.RecipeMapper;
import dev.cats.cookapp.models.RecipeCategory;
import dev.cats.cookapp.repositories.*;
import dev.cats.cookapp.services.TokenExtractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final TokenExtractService tokenExtractService;
    @Override
    public Page<RecipeListResponse> getRecipes(int page, int size, Long userId) {
        var pageRequest = PageRequest.of(page, size);
        var pageOfIds = recipeRepository.findAllIds(pageRequest);
        var recipes = recipeRepository.findAllByIdIn(pageOfIds.getContent(), userId);
        Set<RecipeListResponse> recipeListResponses = recipes.stream()
                .map(objects -> {
                    var recipe = (dev.cats.cookapp.models.Recipe) objects[0];
                    var isSaved = (Boolean) objects[1];
                    Set<RecipeCategory> categories = recipe.getCategories();
                    return new RecipeListResponse(recipe.getId(), recipe.getTitle(), recipe.getPricePerServing(),
                            recipe.getReadyInMinutes(), recipe.getServings(), recipe.getImage(),
                            isSaved, categories);
                })
                .collect(Collectors.toSet());
        return new PageImpl<>(recipeListResponses.stream().toList(),
                pageRequest, pageOfIds.getTotalElements());
    }

    @Override
    public RecipeResponse getRecipe(Long id) {
        var recipe = recipeRepository.findById(id).orElseThrow();
        var recipeResult = recipeMapper.toDto(recipe);
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = tokenExtractService.extractToken(auth).orElseThrow();
        var isSavedForUser = recipe.getLists().stream().anyMatch(userList ->
            userList.getUser().getId().equals(user.getId()));
        recipeResult.setIsSaved(isSavedForUser);
        return recipeResult;
    }
}
