package dev.cats.cookapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
@EnableConfigurationProperties(R2Properties.class)
public class R2Config {
    @Bean
    public S3Presigner s3Presigner(R2Properties props) {
        AwsBasicCredentials creds = AwsBasicCredentials.create(
                props.getAccessKey(), props.getSecretKey()
        );

        return S3Presigner.builder()
                .endpointOverride(URI.create("https://" + props.getAccountId() + ".r2.cloudflarestorage.com"))
                .region(Region.of("auto"))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .chunkedEncodingEnabled(false) // FUCKING CHUNKED ENCODING
                        .build()
                )
                .build();
    }

}
