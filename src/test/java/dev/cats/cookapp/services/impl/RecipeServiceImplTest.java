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
        final Long id = 123L;
        final Recipe expected = new Recipe();
        expected.setId(id);
        when(this.recipeRepository.findById(id)).thenReturn(Optional.of(expected));

        // Act
        final Recipe actual = this.recipeService.getRecipe(id);

        // Assert
        assertThat(actual).isSameAs(expected);
        verify(this.recipeRepository).findById(id);
    }

    @Test
    @DisplayName("getRecipe should throw NoSuchElementException when not found")
    void getRecipe_notFound_throws() {
        // Arrange
        final Long id = 999L;
        when(this.recipeRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> this.recipeService.getRecipe(id))
                .isInstanceOf(NoSuchElementException.class);
        verify(this.recipeRepository).findById(id);
    }

    @Test
    @DisplayName("saveRecipe should delegate to repository and return saved entity")
    void saveRecipe_delegatesToRepository() {
        // Arrange
        final Recipe input = new Recipe();
        input.setTitle("Test");
        final Recipe saved = new Recipe();
        saved.setTitle("Test");
        when(this.recipeRepository.save(input)).thenReturn(saved);

        // Act
        final Recipe result = this.recipeService.saveRecipe(input);

        // Assert
        assertThat(result).isSameAs(saved);
        verify(this.recipeRepository).save(input);
    }

    @Test
    @DisplayName("deleteRecipe should delegate to repository")
    void deleteRecipe_delegatesToRepository() {
        // Arrange
        final Long id = 55L;

        // Act
        this.recipeService.deleteRecipe(id);

        // Assert
        verify(this.recipeRepository).deleteById(id);
    }
}
