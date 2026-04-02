package xyz.learnhub.common.types.config;

import lombok.Data;

@Data
public class S3Config {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String region;
    private String endpoint;
}
