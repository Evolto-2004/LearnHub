package xyz.learnhub.api.controller.backend;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.learnhub.api.event.AdminUserLoginEvent;
import xyz.learnhub.api.request.backend.LoginRequest;
import xyz.learnhub.api.request.backend.PasswordChangeRequest;
import xyz.learnhub.common.annotation.BackendPermission;
import xyz.learnhub.common.annotation.Log;
import xyz.learnhub.common.bus.BackendBus;
import xyz.learnhub.common.config.LearnHubConfig;
import xyz.learnhub.common.constant.BPermissionConstant;
import xyz.learnhub.common.constant.BusinessTypeConstant;
import xyz.learnhub.common.context.BCtx;
import xyz.learnhub.common.domain.AdminUser;
import xyz.learnhub.common.service.AdminUserService;
import xyz.learnhub.common.service.BackendAuthService;
import xyz.learnhub.common.service.RateLimiterService;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.common.util.HelperUtil;
import xyz.learnhub.common.util.IpUtil;
import xyz.learnhub.common.util.MemoryCacheUtil;
import xyz.learnhub.common.util.RequestUtil;

@RestController
@RequestMapping("/backend/v1/auth")
public class LoginController {

    @Autowired private AdminUserService adminUserService;

    @Autowired private BackendBus backendBus;

    @Autowired private BackendAuthService authService;

    @Autowired private ApplicationContext ctx;

    @Autowired private RateLimiterService rateLimiterService;

    @Autowired private LearnHubConfig playEduConfig;

    @PostMapping("/login")
    @Log(title = "管理员-登录", businessType = BusinessTypeConstant.LOGIN)
    public JsonResponse login(@RequestBody @Validated LoginRequest loginRequest) {
        AdminUser adminUser = adminUserService.findByEmail(loginRequest.email);
        if (adminUser == null) {
            return JsonResponse.error("邮箱或密码错误");
        }

        String limitKey = "admin-login-limit:" + loginRequest.getEmail();
        Long reqCount = rateLimiterService.current(limitKey, 3600L);
        if (reqCount > 10 && !playEduConfig.getTesting()) {
            Long exp = MemoryCacheUtil.ttlWithoutPrefix(limitKey);
            return JsonResponse.error(
                    String.format("您的账号已被锁定，请%s后重试", exp > 60 ? exp / 60 + "分钟" : exp + "秒"));
        }

        String password =
                HelperUtil.MD5(loginRequest.getPassword() + adminUser.getSalt()).toLowerCase();
        if (!adminUser.getPassword().equals(password)) {
            return JsonResponse.error("邮箱或密码错误");
        }

        MemoryCacheUtil.del(limitKey);

        if (adminUser.getIsBanLogin().equals(1)) {
            return JsonResponse.error("当前管理员已禁止登录");
        }

        String token = authService.loginUsingId(adminUser.getId(), RequestUtil.url());

        HashMap<String, Object> data = new HashMap<>();
        data.put("token", token);

        ctx.publishEvent(
                new AdminUserLoginEvent(
                        this,
                        adminUser.getId(),
                        token,
                        IpUtil.getIpAddress(),
                        adminUser.getLoginTimes()));

        return JsonResponse.data(data);
    }

    @PostMapping("/logout")
    @Log(title = "管理员-登出", businessType = BusinessTypeConstant.LOGOUT)
    public JsonResponse logout() {
        authService.logout();
        return JsonResponse.success("success");
    }

    @GetMapping("/detail")
    @Log(title = "管理员-详情", businessType = BusinessTypeConstant.GET)
    public JsonResponse detail() {
        AdminUser user = BCtx.getAdminUser();
        HashMap<String, Boolean> permissions = backendBus.adminUserPermissions(user.getId());

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("permissions", permissions);

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.PASSWORD_CHANGE)
    @PutMapping("/password")
    @Log(title = "管理员-密码修改", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse changePassword(@RequestBody @Validated PasswordChangeRequest req) {
        AdminUser user = BCtx.getAdminUser();
        String password = HelperUtil.MD5(req.getOldPassword() + user.getSalt());
        if (!password.equals(user.getPassword())) {
            return JsonResponse.error("原密码不正确");
        }
        adminUserService.passwordChange(user, req.getNewPassword());
        return JsonResponse.success();
    }
}
