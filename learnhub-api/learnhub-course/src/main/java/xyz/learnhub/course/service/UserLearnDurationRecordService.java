package xyz.learnhub.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.learnhub.course.domain.UserLearnDurationRecord;

/**
 * @author tengteng
 * @description 针对表【user_learn_duration_records】的数据库操作Service
 * @createDate 2023-03-20 16:41:12
 */
public interface UserLearnDurationRecordService extends IService<UserLearnDurationRecord> {
    void store(Integer userId, String fromId, String fromScene, Long startTime, Long endTime);

    void remove(Integer userId);
}
