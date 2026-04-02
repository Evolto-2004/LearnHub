package xyz.learnhub.common.service;

public interface RateLimiterService {

    Long current(String key, Long seconds);
}
