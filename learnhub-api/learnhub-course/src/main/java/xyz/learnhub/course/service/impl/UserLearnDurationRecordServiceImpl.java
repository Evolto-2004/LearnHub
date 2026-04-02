package xyz.learnhub.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import xyz.learnhub.course.domain.UserLearnDurationRecord;
import xyz.learnhub.course.mapper.UserLearnDurationRecordMapper;
import xyz.learnhub.course.service.UserLearnDurationRecordService;

/**
 * @author tengteng
 * @description 针对表【user_learn_duration_records】的数据库操作Service实现
 * @createDate 2023-03-20 16:41:12
 */
@Service
public class UserLearnDurationRecordServiceImpl
        extends ServiceImpl<UserLearnDurationRecordMapper, UserLearnDurationRecord>
        implements UserLearnDurationRecordService {

    @Override
    @SneakyThrows
    public void store(
            Integer userId, String fromId, String fromScene, Long startTime, Long endTime) {
        // 处理日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date(endTime));

        UserLearnDurationRecord record = new UserLearnDurationRecord();
        record.setUserId(userId);
        record.setFromId(fromId);
        record.setFromScene(fromScene);
        record.setStartAt(new Date(startTime));
        record.setEndAt(new Date(endTime));
        record.setDuration((int) (endTime - startTime));
        record.setCreatedDate(simpleDateFormat.parse(date));

        save(record);
    }

    @Override
    public void remove(Integer userId) {
        remove(query().getWrapper().eq("user_id", userId));
    }
}
