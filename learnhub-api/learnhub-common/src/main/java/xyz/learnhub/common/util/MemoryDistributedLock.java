package xyz.learnhub.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class MemoryDistributedLock {
    private static final Map<String, LockInfo> locks = new ConcurrentHashMap<>();

    public boolean tryLock(String key, long expire, TimeUnit timeUnit) {
        LockInfo lockInfo = locks.get(key);
        if (lockInfo == null || lockInfo.isExpired()) {
            locks.put(
                    key,
                    new LockInfo(
                            Thread.currentThread().getId(),
                            System.currentTimeMillis() + timeUnit.toMillis(expire)));
            return true;
        }
        return false;
    }

    public void releaseLock(String key) {
        LockInfo lockInfo = locks.get(key);
        if (lockInfo != null && lockInfo.getThreadId() == Thread.currentThread().getId()) {
            locks.remove(key);
        }
    }

    private static class LockInfo {
        private final long threadId;
        private final long expireTime;

        public LockInfo(long threadId, long expireTime) {
            this.threadId = threadId;
            this.expireTime = expireTime;
        }

        public long getThreadId() {
            return threadId;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }
}
