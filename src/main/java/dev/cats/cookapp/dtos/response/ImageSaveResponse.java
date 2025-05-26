package dev.cats.cookapp.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageSaveResponse {
    private String putPreSignedUrl;
    private String publicUrl;
}
