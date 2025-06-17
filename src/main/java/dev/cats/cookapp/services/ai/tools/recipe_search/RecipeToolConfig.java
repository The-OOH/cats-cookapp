package dev.cats.cookapp.services.ai.tools.recipe_search;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cats.cookapp.client.RecipeApiClient;
import dev.cats.cookapp.dtos.external.response.FiltersPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RecipeToolConfig {

    private final RecipeApiClient api;
    private final RecipeSearchTool recipeSearchTool;
    private final ObjectMapper mapper;

    @Bean
    MethodToolCallback searchRecipesCallback() throws Exception {
        final FiltersPayload f = this.api.getFilters().block();

        final Map<String, Object> schema = Map.of(
                "type", "object",
                "properties", Map.of(
                        "searchQuery", Map.of("type", "string", "description", "User's search request in 1 sentence. For example: Chicken soup with chicken, potatoes, carrots, onion, celery, garlic, tomatoes, and spices. If user ask to create a recipe based on ingredients, use this field with list of ingredients by comma separated. For example: Chicken, potatoes, carrots, onion, celery, garlic, tomatoes, spices."),
                        "excludeIngredientIds", Map.of("type", "array",
                                "items", Map.of("type", "integer", "description", "Ingredient IDs to exclude from the search. Use this field ONLY for exclusions by user allergic to ingredients. Based on result of 'search_ingredients' tool")),
                        "difficulties", Map.of("type", "array", "items", Map.of("type", "string", "enum", f.data().difficulties(), "description", "Difficulty levels to include in the search. Based on user's request and ONLY in range of available difficulties. If not provided, use all available difficulties. If provided 'medium' for example, it will return recipes with 'easy' and 'medium' difficulties")),
                        "maxCookingTime", Map.of("type", "integer", "description", "Maximum cooking time in minutes. If not provided, use 1 as default value", "minimum", 1),
                        "minCookingTime", Map.of("type", "integer", "description", "Minimum cooking time in minutes. If not provided, use 120 as default value", "minimum", 1)
                ),
                "required", List.of("searchQuery", "maxCookingTime", "difficulties")
        );

        final Method method = RecipeSearchTool.class.getMethod(
                "searchRecipes",
                String.class,               // searchQuery (single)
                List.class,                 // excludeIngredientIds
                List.class,               // difficulty
                Integer.class,              // maxCookingTime
                Integer.class,              // minCookingTime
                ToolContext.class);

        return MethodToolCallback.builder()
                .toolMethod(method)
                .toolDefinition(ToolDefinition.builder()
                        .name("search_recipes")
                        .description("You need to use this tool when the user wants to get recipe recommendations based on user's request. Not generate a recipe or extract recipe from a web page or any other source." +
                                "To call this tool, you need to ask for the max cooking time and max difficulty one by one" +
                                "If user want to create a recipe based on ingredients, ask for ingredients list, that will be used to search recipe" +
                                "Never ask user about dietary needs, preferred categories. That information is generated based on user's preferences(if it isnt provided than leave field empty)." +
                                "ALWAYS BEFORE this tool call use the 'search_ingredients' tool to get the list of ingredients and their IDs and use it to create excludeIngredientIds list, if excludeIngredientIds exist. If not, use empty list. NEVER TELL user about this step or that you cannot find ids for ingredients.")
                        .inputSchema(this.mapper.writeValueAsString(schema))
                        .build())
                .toolObject(this.recipeSearchTool)
                .build();
    }
}
