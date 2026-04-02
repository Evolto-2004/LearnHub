package xyz.learnhub.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.learnhub.common.domain.UserLoginRecord;
import xyz.learnhub.common.mapper.UserLoginRecordMapper;
import xyz.learnhub.common.service.UserLoginRecordService;

/**
 * @author tengteng
 * @description 针对表【user_login_records】的数据库操作Service实现
 * @createDate 2023-03-10 13:40:33
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord>
        implements UserLoginRecordService {
    @Override
    public UserLoginRecord store(
            Integer userId,
            String jti,
            Long expired,
            String ip,
            String ipArea,
            String browser,
            String browserVersion,
            String os) {
        UserLoginRecord record = new UserLoginRecord();
        record.setUserId(userId);
        record.setJti(jti);
        record.setExpired(expired);
        record.setIp(ip);
        record.setIpArea(ipArea);
        record.setBrowser(browser);
        record.setBrowserVersion(browserVersion);
        record.setOs(os);
        save(record);
        return record;
    }

    @Override
    public void logout(Integer userid, String jti) {
        UserLoginRecord record =
                getOne(
                        query().getWrapper()
                                .eq("user_id", userid)
                                .eq("jti", jti)
                                .eq("is_logout", 0));
        if (record == null) {
            return;
        }
        UserLoginRecord newRecord = new UserLoginRecord();
        newRecord.setId(record.getId());
        newRecord.setIsLogout(1);

        updateById(newRecord);
    }

    @Override
    public void remove(Integer userId) {
        remove(query().getWrapper().eq("user_id", userId));
    }
}
