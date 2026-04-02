package xyz.learnhub.api.controller.frontend;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.learnhub.api.bus.LoginBus;
import xyz.learnhub.api.cache.LoginLimitCache;
import xyz.learnhub.api.cache.LoginLockCache;
import xyz.learnhub.api.event.UserLogoutEvent;
import xyz.learnhub.api.request.frontend.LoginPasswordRequest;
import xyz.learnhub.common.context.FCtx;
import xyz.learnhub.common.domain.User;
import xyz.learnhub.common.exception.LimitException;
import xyz.learnhub.common.service.*;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.common.util.*;

@RestController
@RequestMapping("/api/v1/auth/login")
@Slf4j
public class LoginController {

    @Autowired private UserService userService;

    @Autowired private FrontendAuthService authService;

    @Autowired private ApplicationContext ctx;

    @Autowired private AppConfigService appConfigService;

    @Autowired private LoginBus loginBus;

    @Autowired private LoginLimitCache loginLimitCache;

    @Autowired private LoginLockCache loginLockCache;

    @PostMapping("/password")
    @SneakyThrows
    public JsonResponse password(@RequestBody @Validated LoginPasswordRequest req)
            throws LimitException {
        String email = req.getEmail();

        User user = userService.find(email);
        if (user == null) {
            return JsonResponse.error("邮箱或密码错误");
        }

        loginLimitCache.check(email);

        if (!HelperUtil.MD5(req.getPassword() + user.getSalt()).equals(user.getPassword())) {
            return JsonResponse.error("邮箱或密码错误");
        }

        if (user.getIsLock() == 1) {
            return JsonResponse.error("当前学员已锁定无法登录");
        }

        loginLimitCache.destroy(email);

        return JsonResponse.data(loginBus.tokenByUser(user));
    }

    @PostMapping("/logout")
    public JsonResponse logout() {
        authService.logout();
        ctx.publishEvent(new UserLogoutEvent(this, FCtx.getId(), FCtx.getJwtJti()));
        return JsonResponse.success();
    }
}
