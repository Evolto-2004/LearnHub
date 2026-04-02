package xyz.learnhub.api.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.learnhub.common.config.LearnHubConfig;
import xyz.learnhub.common.exception.ServiceException;
import xyz.learnhub.common.service.RateLimiterService;
import xyz.learnhub.common.util.MemoryCacheUtil;

@Component
public class LoginLimitCache {

    @Autowired private RateLimiterService rateLimiterService;

    @Autowired private LearnHubConfig playEduConfig;

    public void check(String email) throws ServiceException {
        String limitKey = cacheKey(email);
        Long reqCount = rateLimiterService.current(limitKey, 600L);
        if (reqCount >= 10 && !playEduConfig.getTesting()) {
            Long exp = MemoryCacheUtil.ttlWithoutPrefix(limitKey);
            String msg = String.format("您的账号已被锁定，请%s后重试", exp > 60 ? exp / 60 + "分钟" : exp + "秒");
            throw new ServiceException(msg);
        }
    }

    public void destroy(String email) {
        MemoryCacheUtil.del(cacheKey(email));
    }

    private String cacheKey(String email) {
        return "login-limit:" + email;
    }
}
