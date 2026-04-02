package xyz.learnhub.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.types.paginate.UserCourseRecordPaginateFilter;
import xyz.learnhub.course.domain.UserCourseRecord;

/**
 * @author tengteng
 * @description 针对表【user_course_records】的数据库操作Mapper
 * @createDate 2023-03-20 16:41:04
 */
@Mapper
public interface UserCourseRecordMapper extends BaseMapper<UserCourseRecord> {
    List<UserCourseRecord> paginate(UserCourseRecordPaginateFilter filter);

    long paginateTotal(UserCourseRecordPaginateFilter filter);
}
