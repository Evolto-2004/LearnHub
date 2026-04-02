package xyz.learnhub.api.listener;

import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.UserLoginEvent;
import xyz.learnhub.common.service.FrontendAuthService;
import xyz.learnhub.common.service.UserLoginRecordService;
import xyz.learnhub.common.util.IpUtil;

@Component
@Slf4j
public class UserLoginListener {

    @Autowired private UserLoginRecordService loginRecordService;

    @Autowired private FrontendAuthService authService;

    @Async
    @EventListener
    public void updateLoginInfo(UserLoginEvent event) {
        String ipArea = IpUtil.getRealAddressByIP(event.getIp());

        HashMap<String, String> tokenData = authService.parse(event.getToken());
        String jti = tokenData.get("jti");
        Long exp = Long.parseLong(tokenData.get("exp"));

        loginRecordService.store(
                event.getUserId(),
                jti,
                exp,
                event.getIp(),
                ipArea,
                event.getUserAgent().getBrowser().toString(),
                event.getUserAgent().getVersion(),
                event.getUserAgent().getOs().toString());
    }
}
