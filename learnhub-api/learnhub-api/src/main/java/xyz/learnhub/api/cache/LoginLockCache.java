package xyz.learnhub.api.cache;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.learnhub.common.util.MemoryDistributedLock;

@Component
public class LoginLockCache {

    @Autowired private MemoryDistributedLock distributedLock;

    public boolean apply(String username) {
        String key = cacheKey(username);
        return distributedLock.tryLock(key, 10L, TimeUnit.SECONDS);
    }

    public void release(String username) {
        distributedLock.releaseLock(cacheKey(username));
    }

    private String cacheKey(String username) {
        return "login-lock:" + username;
    }
}
