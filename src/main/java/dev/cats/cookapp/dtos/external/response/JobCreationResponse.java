package dev.cats.cookapp.dtos.external.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record JobCreationResponse(
        boolean success,
        @JsonInclude(JsonInclude.Include.NON_NULL) Data data,
        @JsonInclude(JsonInclude.Include.NON_NULL) String error
) {
    public record Data(
            String jobId,
            String status,
            String message,
            String jobTitle
    ) {
    }
}