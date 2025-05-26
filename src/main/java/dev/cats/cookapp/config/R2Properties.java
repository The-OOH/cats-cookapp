package dev.cats.cookapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cloudflare.r2")
public class R2Properties {
    private String accessKey;
    private String secretKey;
    private String accountId;
    private String bucketName;
    private String publicUrl;
}