package xyz.learnhub.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.types.mapper.UserCourseHourRecordCourseCountMapper;
import xyz.learnhub.common.types.mapper.UserCourseHourRecordUserCountMapper;
import xyz.learnhub.common.types.mapper.UserCourseHourRecordUserFirstCreatedAtMapper;
import xyz.learnhub.common.types.paginate.UserCourseHourRecordPaginateFilter;
import xyz.learnhub.course.domain.UserCourseHourRecord;

/**
 * @author tengteng
 * @description 针对表【user_course_hour_records】的数据库操作Mapper
 * @createDate 2023-03-20 16:41:08
 */
@Mapper
public interface UserCourseHourRecordMapper extends BaseMapper<UserCourseHourRecord> {
    List<UserCourseHourRecord> getUserLatestRecords(Integer userId, Integer size);

    List<UserCourseHourRecordCourseCountMapper> getUserCourseHourCount(
            Integer userId, List<Integer> courseIds, Integer isFinished);

    List<UserCourseHourRecordUserCountMapper> getUserCourseHourUserCount(
            Integer courseId, List<Integer> userIds, Integer isFinished);

    List<UserCourseHourRecordUserFirstCreatedAtMapper> getUserCourseHourUserFirstCreatedAt(
            Integer courseId, List<Integer> userIds);

    List<UserCourseHourRecord> paginate(UserCourseHourRecordPaginateFilter filter);

    Long paginateCount(UserCourseHourRecordPaginateFilter filter);

    List<UserCourseHourRecord> getUserPerCourseEarliestRecord(Integer userId);

    List<UserCourseHourRecord> getCoursePerUserEarliestRecord(Integer courseId);
}
