package xyz.learnhub.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.types.paginate.CoursePaginateFiler;
import xyz.learnhub.course.domain.Course;

/**
 * @author tengteng
 * @description 针对表【courses】的数据库操作Mapper
 * @createDate 2023-03-20 14:25:31
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    List<Course> paginate(CoursePaginateFiler filer);

    Long paginateCount(CoursePaginateFiler filer);

    List<Course> openCoursesAndShow(Integer limit, List<Integer> categoryIds);
}
