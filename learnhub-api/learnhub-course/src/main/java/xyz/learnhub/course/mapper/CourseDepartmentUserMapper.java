package xyz.learnhub.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.course.domain.CourseDepartmentUser;

/**
 * @author tengteng
 * @description 针对表【course_department_user】的数据库操作Mapper
 * @createDate 2023-02-24 14:53:52
 */
@Mapper
public interface CourseDepartmentUserMapper extends BaseMapper<CourseDepartmentUser> {}
