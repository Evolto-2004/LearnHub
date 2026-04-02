package xyz.learnhub.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AuthConfig {
    @Value("${sa-token.timeout}")
    private Integer expired;
}
