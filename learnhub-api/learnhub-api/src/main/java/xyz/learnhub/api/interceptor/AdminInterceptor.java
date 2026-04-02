package xyz.learnhub.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.learnhub.common.bus.BackendBus;
import xyz.learnhub.common.context.BCtx;
import xyz.learnhub.common.domain.AdminUser;
import xyz.learnhub.common.service.AdminUserService;
import xyz.learnhub.common.service.AppConfigService;
import xyz.learnhub.common.service.BackendAuthService;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.common.util.HelperUtil;

@Component
@Slf4j
@Order(20)
public class AdminInterceptor implements HandlerInterceptor {

    @Autowired private BackendAuthService authService;

    @Autowired private AdminUserService adminUserService;

    @Autowired private BackendBus backendBus;

    @Autowired private AppConfigService configService;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 读取全局配置
        Map<String, String> systemConfig = configService.keyValues();
        BCtx.setConfig(systemConfig);

        if (BackendBus.inUnAuthWhitelist(request.getRequestURI())) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        if (!authService.check()) {
            return responseTransform(response, 401, "请登录");
        }

        AdminUser adminUser = adminUserService.findById(authService.userId());
        if (adminUser == null) {
            return responseTransform(response, 401, "管理员不存在");
        }
        if (adminUser.getIsBanLogin() == 1) {
            return responseTransform(response, 403, "当前管理员禁止登录");
        }

        BCtx.setId(authService.userId());
        BCtx.setAdminUser(adminUser);
        BCtx.setAdminPer(backendBus.adminUserPermissions(adminUser.getId()));

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
        BCtx.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
