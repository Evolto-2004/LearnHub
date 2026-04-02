package xyz.learnhub.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.course.domain.CourseChapter;

/**
 * @author tengteng
 * @description 针对表【course_chapters】的数据库操作Mapper
 * @createDate 2023-02-26 17:34:01
 */
@Mapper
public interface CourseChapterMapper extends BaseMapper<CourseChapter> {}
