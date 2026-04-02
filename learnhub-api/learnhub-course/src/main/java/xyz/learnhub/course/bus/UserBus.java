package xyz.learnhub.course.bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.learnhub.common.domain.Department;
import xyz.learnhub.common.service.DepartmentService;
import xyz.learnhub.common.service.UserService;
import xyz.learnhub.common.util.StringUtil;
import xyz.learnhub.course.service.CourseService;

/**
 *
 * @create 2023/3/20 14:56
 */
@Component
public class UserBus {

    @Autowired private CourseService courseService;

    @Autowired private UserService userService;

    @Autowired private DepartmentService departmentService;

    public boolean canSeeCourse(Integer userId, Integer courseId) {
        List<Integer> courseDepIds = courseService.getDepIdsByCourseId(courseId);
        if (StringUtil.isEmpty(courseDepIds)) {
            // 线上课全部部门=>任何学员都可以学习
            return true;
        }

        // 获取学员所属部门以及所有父级部门
        List<Integer> allDepIds = new ArrayList<>();
        List<Integer> userDepIds = userService.getDepIdsByUserId(userId);
        if (StringUtil.isNotEmpty(userDepIds)) {
            List<Department> departmentList = departmentService.chunk(userDepIds);
            if (StringUtil.isNotEmpty(departmentList)) {
                for (Department dep : departmentList) {
                    allDepIds.add(dep.getId());
                    if (StringUtil.isNotEmpty(dep.getParentChain())) {
                        allDepIds.addAll(
                                Arrays.stream(dep.getParentChain().split(","))
                                        .map(Integer::valueOf)
                                        .toList());
                    }
                }
            }
        }

        if (StringUtil.isEmpty(allDepIds)) {
            return false;
        }
        return CollectionUtils.intersection(courseDepIds, allDepIds).size() > 0;
    }
}
