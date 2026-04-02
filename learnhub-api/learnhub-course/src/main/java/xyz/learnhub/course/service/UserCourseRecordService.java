package xyz.learnhub.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import xyz.learnhub.common.types.paginate.PaginationResult;
import xyz.learnhub.common.types.paginate.UserCourseRecordPaginateFilter;
import xyz.learnhub.course.domain.UserCourseRecord;

/**
 * @author tengteng
 * @description 针对表【user_course_records】的数据库操作Service
 * @createDate 2023-03-20 16:41:04
 */
public interface UserCourseRecordService extends IService<UserCourseRecord> {

    UserCourseRecord find(Integer userId, Integer courseId);

    void storeOrUpdate(Integer userId, Integer courseId, Integer hourCount, Integer finishedCount);

    List<UserCourseRecord> chunk(Integer userId, List<Integer> courseIds);

    List<UserCourseRecord> chunk(List<Integer> userId, List<Integer> courseIds);

    PaginationResult<UserCourseRecord> paginate(
            int page, int size, UserCourseRecordPaginateFilter filter);

    void destroy(Integer courseId, List<Integer> ids);

    void destroy(Integer userId, Integer courseId);

    void destroy(Integer userId);

    void removeByCourseId(Integer courseId);

    List<UserCourseRecord> chunks(List<Integer> ids, List<String> fields);

    void updateUserCourseLearnProgress(Integer userId, Integer courseId, int count);
}
