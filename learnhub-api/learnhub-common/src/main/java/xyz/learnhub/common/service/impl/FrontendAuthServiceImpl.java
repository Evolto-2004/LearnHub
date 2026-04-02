package xyz.learnhub.common.service.impl;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.learnhub.common.constant.SystemConstant;
import xyz.learnhub.common.service.AuthService;
import xyz.learnhub.common.service.FrontendAuthService;

@Service
public class FrontendAuthServiceImpl implements FrontendAuthService {

    @Autowired private AuthService authService;

    @Override
    public String loginUsingId(Integer userId, String loginUrl) {
        return authService.loginUsingId(userId, loginUrl, SystemConstant.JWT_PRV_USER);
    }

    @Override
    public boolean check() {
        return authService.check(SystemConstant.JWT_PRV_USER);
    }

    @Override
    public Integer userId() {
        return authService.userId();
    }

    @Override
    public void logout() {
        authService.logout();
    }

    @Override
    public String jti() {
        return authService.jti();
    }

    @Override
    public HashMap<String, String> parse(String token) {
        return authService.parse(token);
    }
}
