package xyz.learnhub.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import xyz.learnhub.common.types.mapper.UserCourseHourRecordCourseCountMapper;
import xyz.learnhub.common.types.mapper.UserCourseHourRecordUserCountMapper;
import xyz.learnhub.common.types.mapper.UserCourseHourRecordUserFirstCreatedAtMapper;
import xyz.learnhub.common.types.paginate.PaginationResult;
import xyz.learnhub.common.types.paginate.UserCourseHourRecordPaginateFilter;
import xyz.learnhub.course.domain.UserCourseHourRecord;

/**
 * @author tengteng
 * @description 针对表【user_course_hour_records】的数据库操作Service
 * @createDate 2023-03-20 16:41:08
 */
public interface UserCourseHourRecordService extends IService<UserCourseHourRecord> {
    UserCourseHourRecord find(Integer userId, Integer courseId, Integer hourId);

    boolean storeOrUpdate(
            Integer userId,
            Integer courseId,
            Integer hourId,
            Integer duration,
            Integer totalDuration);

    Integer getFinishedHourCount(Integer userId, Integer courseId);

    List<UserCourseHourRecord> getRecords(Integer userId, Integer courseId);

    List<UserCourseHourRecord> getLatestCourseIds(Integer userId, Integer size);

    void removeByCourseId(Integer courseId);

    void remove(Integer userId, Integer courseId);

    void remove(Integer userId);

    void remove(Integer userId, Integer courseId, Integer hourId);

    List<UserCourseHourRecordCourseCountMapper> getUserCourseHourCount(
            Integer userId, List<Integer> courseIds, Integer isFinished);

    List<UserCourseHourRecordUserCountMapper> getUserCourseHourUserCount(
            Integer courseId, List<Integer> userIds, Integer isFinished);

    List<UserCourseHourRecordUserFirstCreatedAtMapper> getUserCourseHourUserFirstCreatedAt(
            Integer courseId, List<Integer> userIds);

    PaginationResult<UserCourseHourRecord> paginate(
            int page, int size, UserCourseHourRecordPaginateFilter filter);

    List<UserCourseHourRecord> getUserPerCourseEarliestRecord(Integer userId);

    List<UserCourseHourRecord> getCoursePerUserEarliestRecord(Integer courseId);
}
