package xyz.learnhub.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.course.domain.UserLearnDurationRecord;

/**
 * @author tengteng
 * @description 针对表【user_learn_duration_records】的数据库操作Mapper
 * @createDate 2023-03-22 13:55:17
 */
@Mapper
public interface UserLearnDurationRecordMapper extends BaseMapper<UserLearnDurationRecord> {}
