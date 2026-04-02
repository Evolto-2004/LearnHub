package xyz.learnhub.system.aspectj;

import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.learnhub.common.annotation.BackendPermission;
import xyz.learnhub.common.bus.BackendBus;
import xyz.learnhub.common.context.BCtx;
import xyz.learnhub.common.types.JsonResponse;

@Aspect
@Component
@Slf4j
public class BackendPermissionAspect {

    @Autowired private BackendBus backendBus;

    @Pointcut("@annotation(xyz.learnhub.common.annotation.BackendPermission)")
    private void doPointcut() {}

    @Around("doPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        BackendPermission middleware = signature.getMethod().getAnnotation(BackendPermission.class);
        Integer adminUserId = BCtx.getId();
        HashMap<String, Boolean> permissions = backendBus.adminUserPermissions(adminUserId);
        if (permissions.get(middleware.slug()) == null) {
            return JsonResponse.error("权限不足", 403);
        }
        return joinPoint.proceed();
    }
}
