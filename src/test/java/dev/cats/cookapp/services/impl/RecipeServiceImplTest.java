package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.models.recipe.Recipe;
import dev.cats.cookapp.repositories.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Test
    @DisplayName("getRecipe should return recipe when found")
    void getRecipe_found_returnsRecipe() {
        // Arrange
        Long id = 123L;
        Recipe expected = new Recipe();
        expected.setId(id);
        when(recipeRepository.findById(id)).thenReturn(Optional.of(expected));

        // Act
        Recipe actual = recipeService.getRecipe(id);

        // Assert
        assertThat(actual).isSameAs(expected);
        verify(recipeRepository).findById(id);
    }

    @Test
    @DisplayName("getRecipe should throw NoSuchElementException when not found")
    void getRecipe_notFound_throws() {
        // Arrange
        Long id = 999L;
        when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> recipeService.getRecipe(id))
                .isInstanceOf(NoSuchElementException.class);
        verify(recipeRepository).findById(id);
    }

    @Test
    @DisplayName("saveRecipe should delegate to repository and return saved entity")
    void saveRecipe_delegatesToRepository() {
        // Arrange
        Recipe input = new Recipe();
        input.setTitle("Test");
        Recipe saved = new Recipe();
        saved.setTitle("Test");
        when(recipeRepository.save(input)).thenReturn(saved);

        // Act
        Recipe result = recipeService.saveRecipe(input);

        // Assert
        assertThat(result).isSameAs(saved);
        verify(recipeRepository).save(input);
    }

    @Test
    @DisplayName("deleteRecipe should delegate to repository")
    void deleteRecipe_delegatesToRepository() {
        // Arrange
        Long id = 55L;

        // Act
        recipeService.deleteRecipe(id);

        // Assert
        verify(recipeRepository).deleteById(id);
    }
}
