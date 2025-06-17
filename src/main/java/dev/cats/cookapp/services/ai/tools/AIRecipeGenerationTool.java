package dev.cats.cookapp.services.ai.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cats.cookapp.client.IngredientApiClient;
import dev.cats.cookapp.client.PreferencesApiClient;
import dev.cats.cookapp.dtos.external.response.IngredientSearchResponse;
import dev.cats.cookapp.dtos.external.response.preferences.PreferencesPayload;
import dev.cats.cookapp.dtos.request.recipe.RecipeAIRequest;
import dev.cats.cookapp.dtos.response.recipe.CategoryResponse;
import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;
import dev.cats.cookapp.dtos.response.recipe.ingredient.UnitResponse;
import dev.cats.cookapp.models.recipe.RecipeSource;
import dev.cats.cookapp.services.CategoryService;
import dev.cats.cookapp.services.RecipeAPIService;
import dev.cats.cookapp.services.UnitService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AIRecipeGenerationTool {

    private final ChatClient.Builder chatBuilder;
    private final IngredientApiClient ingredientApiClient;
    private final PreferencesApiClient preferencesApiClient;
    private final CategoryService categoryService;
    private final UnitService unitService;
    private final RecipeAPIService recipeService;
    private final Validator validator;
    private final ObjectMapper mapper;

    @Tool(
            name = "generate_recipe",
            description = """
                    Use when you need to create a **new recipe** given dish name(name, description or both), \
                    cooking time, difficulty, ingredient list and/or user preferences. NEVER use this tool when user want to extract recipe from IMAGE(imageUrl) or VIDEO(videoUrl).
                    Also use this tool when user want to structure their text recipe.
                    Returns a recipe JSON object.
                    """
    )
    public String generateRecipe(
            @ToolParam(description = "Comma-separated ingredient names. This list should contain all the ingredients " +
                    "that need for the recipe. NOT only the ingredients that user wants to add. ALL the ingredients " +
                    "that are needed for the recipe should be included in this list.", required = true) final
            String ingredients,

            @ToolParam(description = "Dish name or description of the dish. Provided by the user. If user provides unstructured " +
                    "text for recipe as dish name or description, please add ALL text as the value for this parameter", required = true) final
            String dish,

            @ToolParam(description = "Cooking time in minutes. If not provided by user use 120", required = false) final
            Integer cookingTime,

            @ToolParam(description = "Difficulty: easy | medium | hard", required = true) final
            String difficulty,

            @ToolParam(description = "Comma-separated category names", required = false) final
            String categoriesCsv,

            final ToolContext ctx
    ) throws Exception {
        AIRecipeGenerationTool.log.info("Generate recipe tool. Params: ingredients: {}, dish: {}, cookingTime: {}, difficulty: {}, categoriesCsv: {}",
                ingredients, dish, cookingTime, difficulty, categoriesCsv);
        String userId = Objects.toString(ctx.getContext().get("userId"), "");

        final List<String> ingNames = Arrays.stream(ingredients.split(","))
                .map(String::trim).filter(s -> !s.isEmpty())
                .toList();

        final List<IngredientSearchResponse.Result> ingredientsDetails =
                this.ingredientApiClient.searchIngredients(ingNames, userId).block();

        final PreferencesPayload prefs = this.preferencesApiClient.getPreferences(userId).doOnError(e -> {
            AIRecipeGenerationTool.log.error("Failed to get user preferences", e);
            throw new RuntimeException("Failed to get user preferences", e);
        }).block();

        final List<String> catNames = Optional.ofNullable(categoriesCsv)
                .map(csv -> Arrays.stream(csv.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList())
                .orElse(List.of());

        final Map<String, List<CategoryResponse>> catsGrouped =
                this.categoryService.findByNames(catNames);

        final List<UnitResponse> unitList = this.unitService.findAll();

        final ChatClient client = this.chatBuilder.build();
        RecipeResponse savedRecipe = null;
        String lastErrorMessage = "";
        RecipeAIRequest prevRequest = null;

        for (int attempt = 1; true; attempt++) {
            final String prompt = this.formPrompt(
                    dish, cookingTime, difficulty,
                    ingredientsDetails, prefs, catsGrouped, unitList, prevRequest, lastErrorMessage
            );

            try {
                final ChatResponse resp = client.prompt(prompt).call().chatResponse();
                final String json = resp.getResult().getOutput().getText();

                final RecipeAIRequest req = this.mapper.readValue(json, RecipeAIRequest.class);
                req.setSource(RecipeSource.AI);
                prevRequest = req;

                final Set<ConstraintViolation<RecipeAIRequest>> violations =
                        this.validator.validate(req);
                if (!violations.isEmpty()) {
                    throw new IllegalArgumentException("bean-validation failed: " + violations);
                }

                savedRecipe = this.recipeService.saveAiRecipe(req, userId);
                break;

            } catch (final Exception ex) {
                log.warn("generate_recipe attempt {}/3 failed: {}", attempt, ex.getMessage());
                lastErrorMessage = ex.getMessage();
                if (3 == attempt) throw ex;
            }
        }

        final Map<String, Object> wrapper = Map.of(
                "recipeId", savedRecipe.getId(),
                "recipe", savedRecipe
        );
        return "Use the following JSON to generate a recipe with messageType \"RECIPE_DETAILS\":\n" + this.mapper.writeValueAsString(wrapper);
    }

    private String formPrompt(
            final String dish,
            final Integer cookingTime,
            final String difficulty,
            final List<IngredientSearchResponse.Result> ingDetails,
            final PreferencesPayload prefs,
            final Map<String, List<CategoryResponse>> categories,
            final List<UnitResponse> unitIds,
            final RecipeAIRequest prevRequest,
            final String lastErrorMessage
    ) throws Exception {

        final String ingJson = this.mapper.writeValueAsString(ingDetails);
        final String prefJson = this.mapper.writeValueAsString(prefs);
        final String catJson = this.mapper.writeValueAsString(categories);
        final String unitJson = this.mapper.writeValueAsString(unitIds);

        final String prevRequestJson = prevRequest == null ? "null" : this.mapper.writeValueAsString(prevRequest);

        final String errorMessage;

        if (!lastErrorMessage.isBlank()) {
            errorMessage = String.format("IMPOPTANT: Last error message: %s. Previous request: %s. Based on this, try to generate correct recipe.", lastErrorMessage, prevRequestJson);
        }
        else {
            errorMessage = "";
        }

        return """
                ## Core Instructions
                You are a culinary AI. Generate a *valid* JSON object that matches the example schema.
                Use ONLY ids that exist in the supplied ingredient / category / unit lists.
                It is MANDATORY to use ONLY the supplied ingredients, categories, and units.
                
                **Parameters:**
                - Dish: %s
                - Total time (min): %d
                - Difficulty: %s
                
                **Data Sources:**
                - Available ingredients JSON: %s
                - User preferences JSON: %s
                - Category lookup: %s
                - Unit IDs (metric preferred): %s
                
                **Return nothing else, just the JSON.**
                
                ## Enhanced Context
                Adopt the role of an expert chef and culinary writer with a deep understanding of global cuisines. You will create a detailed food recipe that caters to specific dietary preferences or cuisines based on user input. Your recipe should be easy to follow, include detailed instructions, and focus on maximizing flavor and creativity while considering the dietary or cuisine-specific needs.
                
                ## Goal
                Generate a food recipe that aligns with the input provided, including all necessary steps, ingredients, and cooking techniques. The recipe should be structured for ease of understanding and execution, regardless of the reader's culinary expertise.
                
                ## Response Guidelines
                Follow this step-by-step approach:
                
                1. **Core Elements**: Identify the main ingredient and its role in the dish
                2. **Ingredient Categories**: Organize ingredients by type (fresh produce, spices, oils, proteins, etc.)
                3. **Detailed Instructions**: Provide step-by-step preparation methods, cooking techniques, and timing
                4. **Dietary Adaptations**: Highlight tips for dietary restrictions (gluten-free, vegan, low-carb)
                5. **Complementary Pairings**: Suggest sides or accompaniments
                6. **Equipment & Substitutions**: Mention necessary tools and offer alternatives
                7. **Precise Measurements**: Ensure accuracy in quantities and timing
                8. **Presentation Ideas**: Include plating suggestions for visual appeal
                
                ## JSON Schema
                ```json
                {
                  "title": "text",
                  "description": "text",
                  "source": "AI", // ALWAYS AI
                  "difficulty": "easy", // easy, medium, hard
                  "duration": 30, // total cooking time in minutes
                  "servings": 2, // number of servings
                  "categories": [1, 2], // category ids
                  "ingredients": [
                     {"productId": 11, "measurements": {"unitId": 4, "amount": 100}}
                  ],
                  "steps": [{"stepNumber": 1, "description": "text"}]
                }
                ```
                
                ## Quality Requirements
                - Use simple, approachable language suitable for a wide audience
                - Include helpful tips for ingredient substitutions
                - Encourage creativity with variations for different tastes
                - Structure content clearly with logical flow
                - Focus on maximizing flavor while respecting dietary constraints
                - Provide precise measurements and timing guidance
                
                %s
                """.formatted(
                dish, cookingTime, difficulty, ingJson,
                prefJson, catJson, unitJson, errorMessage
        );
    }
}
