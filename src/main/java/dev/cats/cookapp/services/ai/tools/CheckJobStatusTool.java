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
public class CheckJobStatusTool {

    private final ExtractionJobsApiClient extractionJobsApiClient;
    private final ObjectMapper            mapper;

    @Tool(
            name        = "check_job_status",
            description = """
            Use when you need to check the status of a background job.
            Returns a JSON object with background job information or the result of the extraction.
            """
    )
    public String generateRecipeFromImage(
            @ToolParam(description = "Background job id", required = true) final
            String jobId,
            final ToolContext ctx
    ) throws Exception {
        CheckJobStatusTool.log.info("check_job_status tool. Params: jobId: {}", jobId);

        final String userId = Objects.toString(ctx.getContext().get("userId"), "");

        try {
            final var res = this.extractionJobsApiClient.getJobStatus(jobId, userId).block();
            return this.mapper.writeValueAsString(Map.of("result", null != res ? res : "")) + "\n" +
                    "Based on this result provide user information about the recipe extraction. If recipeId is present, ALWAYS IMMEDIATELY use 'recipe_details' tool to get generated recipe details and provide user with the recipe.";
        }
        catch (final Exception ex) {
            CheckJobStatusTool.log.error("Failed to get job status", ex);
            return "Sorry, I couldn't get job status. Please try again.";
        }

    }
}
