package dev.cats.cookapp.services.ai.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cats.cookapp.client.ExtractionJobsApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class VideoRecipeGenerationTool {

    private final ExtractionJobsApiClient extractionJobsApiClient;
    private final ObjectMapper mapper;

    @Tool(
            name = "extract_recipe_from_video",
            description = """
                    ALWAYS Use when you need to extract a recipe from an cooking video from Instagram or TikTok(ONLY for these platforms).
                    ALWAYS Use this tool when user provide video url.
                    Returns a JSON object with background job information or the result of the extraction.
                    """
    )
    public String generateRecipeFromVideo(
            @ToolParam(description = "Video url from Instagram or TikTok ONLY", required = true) final
            String videoUrl,
            final ToolContext ctx
    ) throws Exception {
        VideoRecipeGenerationTool.log.info("extract_recipe_from_video tool. Params: videoUrl: {}", videoUrl);

        final String userId = Objects.toString(ctx.getContext().get("userId"), "");

        try {
            final var res = this.extractionJobsApiClient.submitVideo(videoUrl, 10, userId).block();
            return this.mapper.writeValueAsString(Map.of("result", null != res ? res : "")) + "\n" +
                    "Tell user, that recipe extraction is started and they can ask for the status later or check the result in background jobs tab. Return message with messageType \"JOB_STATUS\" and jobInfo";
        } catch (final Exception ex) {
            VideoRecipeGenerationTool.log.error("Failed to extract recipe from video", ex);
            return "Sorry, I couldn't extract recipe from this video. Please try again.";
        }

    }
}
