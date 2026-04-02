package xyz.learnhub.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class LearnHubConfig {

    @Value("${spring.profiles.active}")
    private String env;

    @Value("${learnhub.core.testing}")
    private Boolean testing;

    @Value("${learnhub.limiter.duration}")
    private Long limiterDuration;

    @Value("${learnhub.limiter.limit}")
    private Long limiterLimit;
}
