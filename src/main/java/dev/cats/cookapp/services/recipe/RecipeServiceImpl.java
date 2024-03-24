package dev.cats.cookapp.services.recipe;

import dev.cats.cookapp.dto.response.RecipeResponse;
import dev.cats.cookapp.mappers.RecipeMapper;
import dev.cats.cookapp.models.*;
import dev.cats.cookapp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCategoryRepository recipeCategoryRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;
    private final RecipeCategoryTypeRepository recipeCategoryTypeRepository;
    private final RecipeMapper recipeMapper;
    public List<RecipeResponse> getRecipes() {
        mockInit();
        return recipeRepository.findAll().stream().map(recipeMapper::toDto).toList();
    }

    private void mockInit(){
        try{
            var recipe = new Recipe();
            recipe.setVegetarian(true);
            recipe.setVegan(true);
            recipe.setGlutenFree(true);
            recipe.setDairyFree(true);
            recipe.setTitle("title");
            recipe.setPricePerServing(1);
            recipe.setSpoonacularId(1);
            recipe.setReadyInMinutes(1);
            recipe.setServings(1);
            recipe.setImage("image");
            recipe.setSummary("summary");
            recipe.setCheap(true);
            recipe.setHealthScore(1);
            recipe.setSpoonacularScore(1);
            recipeRepository.save(recipe);

            var recipeIngredient = new RecipeIngredient();
            recipeIngredient.setAmount(1.0);
            recipeIngredient.setOriginal("original");

            var unit = new Unit();
            unit.setName("name");
            unit.setIsMetric(true);
            unitRepository.save(unit);
            recipeIngredient.setUnit(unit);

            var product = new Product();
            product.setName("name");
            product.setAisle("aisle");
            product.setImage("image");
            productRepository.save(product);

            recipeIngredient.setProduct(product);
            recipeIngredient.setRecipe(recipe);
            recipe.getProducts().add(recipeIngredient);
            recipeIngredientRepository.save(recipeIngredient);

            recipeRepository.save(recipe);

            var category = new RecipeCategory();
            category.setName("name");
            var type = new RecipeCategoryType();
            type.setName("name");
            recipeCategoryTypeRepository.save(type);
            category.setRecipeCategoryType(type);
            recipe.getCategories().add(category);
            recipeCategoryRepository.save(category);
            recipeRepository.save(recipe);
        }
        catch (Exception e) {
            System.out.println("Error in mockInit");
        }
    }
}
