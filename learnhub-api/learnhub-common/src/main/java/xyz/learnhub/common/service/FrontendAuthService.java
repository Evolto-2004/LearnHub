package xyz.learnhub.common.service;

import java.util.HashMap;

public interface FrontendAuthService {
    String loginUsingId(Integer userId, String loginUrl);

    boolean check();

    Integer userId();

    void logout();

    String jti();

    HashMap<String, String> parse(String token);
}
