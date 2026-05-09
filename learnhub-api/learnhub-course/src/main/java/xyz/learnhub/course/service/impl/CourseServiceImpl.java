package xyz.learnhub.course.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.common.types.paginate.CoursePaginateFiler;
import xyz.learnhub.common.types.paginate.PaginationResult;
import xyz.learnhub.common.util.StringUtil;
import xyz.learnhub.course.domain.Course;
import xyz.learnhub.course.domain.CourseDepartmentUser;
import xyz.learnhub.course.mapper.CourseMapper;
import xyz.learnhub.course.service.CourseDepartmentUserService;
import xyz.learnhub.course.service.CourseService;

/**
 * @author tengteng
 * @description 针对表【courses】的数据库操作Service实现
 * @createDate 2023-02-24 14:14:01
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired private CourseDepartmentUserService courseDepartmentUserService;

    @Override
    public PaginationResult<Course> paginate(int page, int size, CoursePaginateFiler filter) {
        filter.setPageStart((page - 1) * size);
        filter.setPageSize(size);

        PaginationResult<Course> pageResult = new PaginationResult<>();
        pageResult.setData(getBaseMapper().paginate(filter));
        pageResult.setTotal(getBaseMapper().paginateCount(filter));

        return pageResult;
    }

    @Override
    @Transactional
    public Course createWithDepIds(
            String title,
            Integer thumb,
            String shortDesc,
            Integer isRequired,
            Integer isShow,
            Integer[] depIds,
            Integer adminId) {
        Course course = new Course();
        course.setTitle(title);
        course.setThumb(thumb);
        course.setShortDesc(shortDesc);
        course.setIsShow(isShow);
        course.setIsRequired(isRequired);
        course.setSortAt(new Date());
        course.setCreatedAt(new Date());
        course.setUpdatedAt(new Date());
        course.setAdminId(adminId);
        save(course);

        relateDepartments(course, depIds);

        return course;
    }

    @Override
    public void relateDepartments(Course course, Integer[] depIds) {
        if (depIds == null || depIds.length == 0) {
            return;
        }
        List<CourseDepartmentUser> courseDepartmentUsers = new ArrayList<>();
        for (Integer depId : depIds) {
            courseDepartmentUsers.add(
                    new CourseDepartmentUser() {
                        {
                            setCourseId(course.getId());
                            setRangeId(depId);
                        }
                    });
        }
        courseDepartmentUserService.saveBatch(courseDepartmentUsers);
    }

    @Override
    public void resetRelateDepartments(Course course, Integer[] depIds) {
        courseDepartmentUserService.removeByCourseId(course.getId());
        relateDepartments(course, depIds);
    }

    @Override
    @Transactional
    public void updateWithDepIds(
            Course course,
            String title,
            Integer thumb,
            String shortDesc,
            Integer isRequired,
            Integer isShow,
            String sortAt,
            Integer[] depIds) {
        Course newCourse = new Course();
        newCourse.setId(course.getId());
        newCourse.setTitle(title);
        newCourse.setThumb(thumb);
        newCourse.setIsShow(isShow);
        newCourse.setIsRequired(isRequired);
        newCourse.setShortDesc(shortDesc);

        if (StringUtil.isNotEmpty(sortAt)) {
            newCourse.setSortAt(DateUtil.parseDate(sortAt));
        }

        updateById(newCourse);

        resetRelateDepartments(course, depIds);
    }

    @Override
    public Course findOrFail(Integer id) throws NotFoundException {
        Course course = getOne(query().getWrapper().eq("id", id));
        if (course == null) {
            throw new NotFoundException("课程不存在");
        }
        return course;
    }

    @Override
    public List<Integer> getDepIdsByCourseId(Integer courseId) {
        return courseDepartmentUserService.getDepIdsByCourseId(courseId);
    }

    @Override
    public void updateClassHour(Integer courseId, Integer classHour) {
        Course course = new Course();
        course.setId(courseId);
        course.setClassHour(classHour);
        updateById(course);
    }

    @Override
    public List<Course> chunks(List<Integer> ids, List<String> fields) {
        return list(query().getWrapper().in("id", ids).select(fields));
    }

    @Override
    public List<Course> chunks(List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return list(query().getWrapper().in("id", ids));
    }

    @Override
    public List<Course> getOpenCoursesAndShow(Integer limit) {
        return getBaseMapper().openCoursesAndShow(limit);
    }

    @Override
    public List<Course> getDepCoursesAndShow(List<Integer> depIds) {
        if (StringUtil.isEmpty(depIds)) {
            return new ArrayList<>();
        }
        List<Integer> courseIds = courseDepartmentUserService.getCourseIdsByDepIds(depIds);
        if (StringUtil.isEmpty(courseIds)) {
            return new ArrayList<>();
        }
        return list(query().getWrapper().in("id", courseIds).eq("is_show", 1));
    }

    @Override
    public Map<Integer, List<Integer>> getDepIdsGroup(List<Integer> courseIds) {
        if (courseIds == null || courseIds.size() == 0) {
            return null;
        }
        Map<Integer, List<CourseDepartmentUser>> data =
                courseDepartmentUserService
                        .list(
                                courseDepartmentUserService
                                        .query()
                                        .getWrapper()
                                        .in("course_id", courseIds))
                        .stream()
                        .collect(Collectors.groupingBy(CourseDepartmentUser::getCourseId));
        Map<Integer, List<Integer>> result = new HashMap<>();
        data.forEach(
                (courseId, records) -> {
                    result.put(
                            courseId,
                            records.stream().map(CourseDepartmentUser::getRangeId).toList());
                });
        return result;
    }

    @Override
    public Long total() {
        return count();
    }
}
