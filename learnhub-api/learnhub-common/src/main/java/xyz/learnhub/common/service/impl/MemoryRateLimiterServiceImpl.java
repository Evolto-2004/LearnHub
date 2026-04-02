package xyz.learnhub.common.service.impl;

import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Service;
import xyz.learnhub.common.service.RateLimiterService;
import xyz.learnhub.common.util.MemoryCacheUtil;

@Service
public class MemoryRateLimiterServiceImpl implements RateLimiterService {
    private static final ReentrantLock lock = new ReentrantLock();

    @Override
    public Long current(String key, Long duration) {
        lock.lock();
        try {
            Object value = MemoryCacheUtil.get(key);
            if (value == null) {
                // 第一次访问，设置初始值和过期时间
                MemoryCacheUtil.set(key, 1L, duration);
                return 1L;
            }
            // 已存在计数器，直接自增，increment方法已经能处理Long和AtomicLong类型
            return MemoryCacheUtil.increment(key, 1L, duration);
        } finally {
            lock.unlock();
        }
    }
}
