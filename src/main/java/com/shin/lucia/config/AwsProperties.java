package com.shin.lucia.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {
    private String accessKeyId;
    private String secretKey;
    private S3 s3 = new S3();

    @Data
    public static class S3 {
        private String region;
        private String bucket;

    }
}
