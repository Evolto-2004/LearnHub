package xyz.learnhub.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import xyz.learnhub.common.config.UniqueNameGeneratorConfig;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
@ComponentScan(
        basePackages = {"xyz.learnhub"},
        nameGenerator = UniqueNameGeneratorConfig.class)
@MapperScan("xyz.learnhub.**.mapper")
public class LearnHubApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnHubApiApplication.class, args);
    }
}
