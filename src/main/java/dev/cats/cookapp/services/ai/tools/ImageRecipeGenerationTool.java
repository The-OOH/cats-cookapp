package dev.cats.cookapp.services.ai.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cats.cookapp.client.ExtractionJobsApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageRecipeGenerationTool {

    private final ExtractionJobsApiClient extractionJobsApiClient;
    private final ObjectMapper mapper;

    @Tool(
            name = "extract_recipe_from_image",
            description = """
                    ALWAYS Use when you need to extract a recipe from an image of text recipe.
                    ALWAYS Use this tool when user provide image url.
                    Returns a JSON object with background job information or the result of the extraction.
                    """
    )
    public String generateRecipeFromImage(
            @ToolParam(description = "Image url", required = true) final
            String imageUrl,
            final ToolContext ctx
    ) throws Exception {
        ImageRecipeGenerationTool.log.info("extract_recipe_from_image tool. Params: imageUrl: {}", imageUrl);

        final String userId = Objects.toString(ctx.getContext().get("userId"), "");


        try {
            final var res = this.extractionJobsApiClient.submitImage(imageUrl, userId).block();
            return this.mapper.writeValueAsString(Map.of("result", null != res ? res : "")) + "\n" +
                    "Tell user, that recipe extraction is started and they can ask for the status later or check the result in background jobs tab.Return message with messageType \"JOB_START\" and jobInfo";
        } catch (final Exception ex) {
            ImageRecipeGenerationTool.log.error("Failed to extract recipe from image", ex);
            return "Sorry, I couldn't extract recipe from this image. Please try again.";
        }

    }
}
