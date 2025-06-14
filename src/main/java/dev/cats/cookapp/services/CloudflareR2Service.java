package dev.cats.cookapp.services;

import dev.cats.cookapp.dtos.response.ImageSaveResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;
import dev.cats.cookapp.config.R2Properties;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CloudflareR2Service {
    S3Presigner presigner;
    R2Properties props;

    public ImageSaveResponse getPresignedUrl(final String key, final Duration expiry) {
        final PutObjectPresignRequest preq = PutObjectPresignRequest.builder()
                .signatureDuration(expiry)
                .putObjectRequest(r -> r.bucket(this.props.getBucketName()).key(key).build())
                .build();

        return ImageSaveResponse.builder()
                .putPreSignedUrl(this.presigner.presignPutObject(preq).url().toString())
                .publicUrl(this.props.getPublicUrl() + "/" + key)
                .build();
    }
}
