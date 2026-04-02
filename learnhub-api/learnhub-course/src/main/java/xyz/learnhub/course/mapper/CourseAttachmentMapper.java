package xyz.learnhub.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.course.domain.CourseAttachment;

/**
 * @author tengteng
 * @description 针对表【course_attachment】的数据库操作Mapper
 * @createDate 2023-08-02 17:34:01
 */
@Mapper
public interface CourseAttachmentMapper extends BaseMapper<CourseAttachment> {}
