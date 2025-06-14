package dev.cats.cookapp.services.ai.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cats.cookapp.client.IngredientApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class IngredientSearchTool {
    private final IngredientApiClient api;
    private final ObjectMapper mapper;

    @Tool(
        name        = "ingredient_search",
        description = """
            Call only after the user has given you free-text ingredients.
            Returns a JSON array of ingredient IDs you must feed into generate_recipe tool.
            ALWAYS use this tool before using 'generate_recipe' tool to get the list of ingredients and their IDs and use it in recipe ingredients list
            """)
    public String ingredientSearch(
            @ToolParam(
                    description = "List of ingredient names. Comma separated",
                    required = true
            ) final
            String ingredients,
            final ToolContext ctx)
            throws Exception {
        final String userId = ctx.getContext().get("userId").toString();
        final List <String> ingredientsList = Arrays.asList(ingredients.split(","));
        IngredientSearchTool.log.info("Searching ingredients: {}", ingredients);
        return this.mapper.writeValueAsString(this.api.searchIngredients(ingredientsList, userId).block()) + "\n" + "Immediately you NEED to call tool 'search_recipes' to get recipe recommendations now based on this result";
    }
}
