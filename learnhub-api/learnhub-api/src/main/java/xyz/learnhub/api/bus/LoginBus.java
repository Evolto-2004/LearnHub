package xyz.learnhub.api.bus;

import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.UserLoginEvent;
import xyz.learnhub.common.domain.User;
import xyz.learnhub.common.service.*;
import xyz.learnhub.common.util.IpUtil;
import xyz.learnhub.common.util.RequestUtil;

@Component
@Slf4j
public class LoginBus {

    @Autowired private FrontendAuthService authService;

    @Autowired private ApplicationContext ctx;

    public HashMap<String, Object> tokenByUser(User user) {
        String token = authService.loginUsingId(user.getId(), RequestUtil.url());

        HashMap<String, Object> data = new HashMap<>();
        data.put("token", token);

        ctx.publishEvent(
                new UserLoginEvent(
                        this,
                        user.getId(),
                        user.getEmail(),
                        token,
                        IpUtil.getIpAddress(),
                        RequestUtil.ua()));

        return data;
    }
}
