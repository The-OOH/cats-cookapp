package dev.cats.cookapp.client;

import dev.cats.cookapp.dtos.external.request.ImageProcessingRequest;
import dev.cats.cookapp.dtos.external.request.VideoProcessingRequest;
import dev.cats.cookapp.dtos.external.response.JobCreationResponse;
import dev.cats.cookapp.dtos.external.response.JobStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExtractionJobsApiClient {

    private final WebClient client;

    public Mono<JobCreationResponse.Data> submitImage(final String imageUrl, final String userId) {
        final ImageProcessingRequest body = new ImageProcessingRequest(imageUrl);
        return this.client.post()
                .uri("/recipe-sources/image-url")
                .header("x-user-id", userId)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JobCreationResponse.class)
                .map(JobCreationResponse::data);
    }

    public Mono<JobCreationResponse.Data> submitVideo(final String videoUrl,
                                                      final int frameCount,
                                                      final String userId) {
        final VideoProcessingRequest body = new VideoProcessingRequest(videoUrl, frameCount);
        return this.client.post()
                .uri("/recipe-sources/video-url")
                .header("x-user-id", userId)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JobCreationResponse.class)
                .map(JobCreationResponse::data);
    }

    public Mono<JobStatusResponse.Data> getJobStatus(final String jobId, final String userId) {
        return this.client.get()
                .uri("/jobs/{jobId}/status", jobId)
                .header("x-user-id", userId)
                .retrieve()
                .bodyToMono(JobStatusResponse.class)
                .map(JobStatusResponse::data);
    }
}
