package xyz.learnhub.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.course.domain.CourseCategory;

/**
 * @author tengteng
 * @description 针对表【course_category】的数据库操作Mapper
 * @createDate 2023-03-09 09:54:22
 */
@Mapper
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {}
