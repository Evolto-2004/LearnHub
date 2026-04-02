package xyz.learnhub.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import xyz.learnhub.course.domain.CourseDepartmentUser;

/**
 * @author tengteng
 * @description 针对表【course_department_user】的数据库操作Service
 * @createDate 2023-02-24 14:53:52
 */
public interface CourseDepartmentUserService extends IService<CourseDepartmentUser> {

    List<Integer> getCourseIdsByDepIds(List<Integer> depIds);

    List<Integer> getDepIdsByCourseId(Integer courseId);

    void removeByCourseId(Integer courseId);

    List<Integer> getCourseIdsByDepId(Integer depId);
}
