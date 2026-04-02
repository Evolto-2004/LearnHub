package xyz.learnhub.common.service;

import java.util.HashMap;

public interface AuthService {
    String loginUsingId(Integer userId, String loginUrl, String prv);

    boolean check(String prv);

    Integer userId();

    void logout();

    String jti();

    Long expired();

    HashMap<String, String> parse(String token);
}
