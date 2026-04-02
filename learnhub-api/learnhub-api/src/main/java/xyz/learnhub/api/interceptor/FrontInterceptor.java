package xyz.learnhub.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.learnhub.common.constant.FrontendConstant;
import xyz.learnhub.common.context.FCtx;
import xyz.learnhub.common.domain.User;
import xyz.learnhub.common.service.FrontendAuthService;
import xyz.learnhub.common.service.UserService;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.common.util.HelperUtil;

@Component
@Slf4j
@Order(20)
public class FrontInterceptor implements HandlerInterceptor {

    @Autowired private FrontendAuthService authService;

    @Autowired private UserService userService;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (FrontendConstant.UN_AUTH_URI_WHITELIST.contains(request.getRequestURI())) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        if (!authService.check()) {
            return responseTransform(response, 401, "请登录");
        }

        User user = userService.find(authService.userId());
        if (user == null) {
            return responseTransform(response, 401, "请重新登录");
        }
        if (user.getIsLock() == 1) {
            return responseTransform(response, 403, "当前学员已锁定无法登录");
        }

        FCtx.setUser(user);
        FCtx.setId(user.getId());
        FCtx.setJWtJti(authService.jti());

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean responseTransform(HttpServletResponse response, int code, String msg)
            throws IOException {
        response.setStatus(code);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(HelperUtil.toJsonStr(JsonResponse.error(msg)));
        return false;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        FCtx.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
