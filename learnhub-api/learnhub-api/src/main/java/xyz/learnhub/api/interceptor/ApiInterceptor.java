package xyz.learnhub.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.learnhub.common.config.LearnHubConfig;
import xyz.learnhub.common.constant.BackendConstant;
import xyz.learnhub.common.service.RateLimiterService;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.common.util.HelperUtil;
import xyz.learnhub.common.util.IpUtil;

@Component
@Slf4j
@Order(10)
public class ApiInterceptor implements HandlerInterceptor {

    @Autowired private RateLimiterService rateLimiterService;

    @Autowired private LearnHubConfig playEduConfig;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return false;
        }

        // 当前api的请求路径
        String path = request.getRequestURI();
        // 白名单过滤
        if (BackendConstant.API_LIMIT_WHITELIST.contains(path)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        // 限流判断
        String reqCountKey = "api-limiter:" + IpUtil.getIpAddress();
        Long reqCount = rateLimiterService.current(reqCountKey, playEduConfig.getLimiterDuration());
        long limitCount = playEduConfig.getLimiterLimit();
        long limitRemaining = limitCount - reqCount;
        response.setHeader("X-RateLimit-Limit", String.valueOf(limitCount));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(limitRemaining));
        if (limitRemaining <= 0) {
            response.setStatus(429);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(HelperUtil.toJsonStr(JsonResponse.error("太多请求")));
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
