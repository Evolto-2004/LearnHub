package xyz.learnhub.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.course.domain.CourseHour;

/**
 * @author tengteng
 * @description 针对表【course_hour】的数据库操作Mapper
 * @createDate 2023-03-15 10:16:45
 */
@Mapper
public interface CourseHourMapper extends BaseMapper<CourseHour> {}
