package xyz.learnhub.system.aspectj;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.learnhub.common.annotation.Lock;
import xyz.learnhub.common.exception.LimitException;
import xyz.learnhub.common.util.MemoryDistributedLock;

@Aspect
@Component
public class LockAspect {
    @Autowired private MemoryDistributedLock distributedLock;

    public LockAspect(MemoryDistributedLock distributedLock) {
        this.distributedLock = distributedLock;
    }

    @Around("@annotation(xyz.learnhub.common.annotation.Lock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Lock lock = method.getAnnotation(Lock.class);
        String key = lock.key();
        long expire = lock.expire();
        TimeUnit timeUnit = lock.timeUnit();
        boolean success = distributedLock.tryLock(key, expire, timeUnit);
        if (!success) {
            throw new LimitException("请稍后再试");
        }
        try {
            return joinPoint.proceed();
        } finally {
            distributedLock.releaseLock(key);
        }
    }
}
