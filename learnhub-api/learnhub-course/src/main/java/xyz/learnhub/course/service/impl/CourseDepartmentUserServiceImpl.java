package xyz.learnhub.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import xyz.learnhub.course.domain.CourseDepartmentUser;
import xyz.learnhub.course.mapper.CourseDepartmentUserMapper;
import xyz.learnhub.course.service.CourseDepartmentUserService;

/**
 * @author tengteng
 * @description 针对表【course_department_user】的数据库操作Service实现
 * @createDate 2023-02-24 14:53:52
 */
@Service
public class CourseDepartmentUserServiceImpl
        extends ServiceImpl<CourseDepartmentUserMapper, CourseDepartmentUser>
        implements CourseDepartmentUserService {
    @Override
    public List<Integer> getCourseIdsByDepIds(List<Integer> depIds) {
        return list(query().getWrapper().in("range_id", depIds)).stream()
                .map(CourseDepartmentUser::getCourseId)
                .toList();
    }

    @Override
    public List<Integer> getDepIdsByCourseId(Integer courseId) {
        return list(query().getWrapper().eq("course_id", courseId)).stream()
                .map(CourseDepartmentUser::getRangeId)
                .toList();
    }

    @Override
    public void removeByCourseId(Integer courseId) {
        remove(query().getWrapper().eq("course_id", courseId));
    }

    @Override
    public List<Integer> getCourseIdsByDepId(Integer depId) {
        return list(query().getWrapper().eq("range_id", depId)).stream()
                .map(CourseDepartmentUser::getCourseId)
                .toList();
    }
}
