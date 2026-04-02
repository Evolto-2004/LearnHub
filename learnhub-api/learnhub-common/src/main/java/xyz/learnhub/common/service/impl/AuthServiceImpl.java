package xyz.learnhub.common.service.impl;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.learnhub.common.config.AuthConfig;
import xyz.learnhub.common.service.AuthService;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired private AuthConfig authConfig;

    @Override
    public String loginUsingId(Integer userId, String loginUrl, String prv) {
        StpUtil.login(
                userId,
                SaLoginConfig.setExtra("url", loginUrl)
                        .setExtra("prv", prv)
                        .setExtra(
                                "exp",
                                String.valueOf(
                                        System.currentTimeMillis()
                                                + authConfig.getExpired() * 1000L)));
        return StpUtil.getTokenValue();
    }

    @Override
    public boolean check(String prv) {
        if (!StpUtil.isLogin()) {
            return false;
        }
        String tokenPrv = (String) StpUtil.getExtra("prv");
        return prv.equals(tokenPrv);
    }

    @Override
    public Integer userId() {
        return StpUtil.getLoginIdAsInt();
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public String jti() {
        return (String) StpUtil.getExtra("rnStr");
    }

    @Override
    public Long expired() {
        return (Long) StpUtil.getExtra("exp");
    }

    @Override
    public HashMap<String, String> parse(String token) {
        HashMap<String, String> data = new HashMap<>();
        data.put("jti", (String) StpUtil.getExtra(token, "rnStr"));
        data.put("exp", (String) StpUtil.getExtra(token, "exp"));
        return data;
    }
}
