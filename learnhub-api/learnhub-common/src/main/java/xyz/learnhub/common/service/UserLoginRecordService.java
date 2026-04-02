package xyz.learnhub.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.learnhub.common.domain.UserLoginRecord;

public interface UserLoginRecordService extends IService<UserLoginRecord> {
    UserLoginRecord store(
            Integer userId,
            String jti,
            Long expired,
            String ip,
            String ipArea,
            String browser,
            String browserVersion,
            String os);

    void logout(Integer userid, String jti);

    void remove(Integer userId);
}
