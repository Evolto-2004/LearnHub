package xyz.learnhub.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.common.types.paginate.CoursePaginateFiler;
import xyz.learnhub.common.types.paginate.PaginationResult;
import xyz.learnhub.course.domain.Course;

/**
 * @author tengteng
 * @description 针对表【courses】的数据库操作Service
 * @createDate 2023-02-24 14:14:01
 */
public interface CourseService extends IService<Course> {

    PaginationResult<Course> paginate(int page, int size, CoursePaginateFiler filter);

    Course createWithCategoryIdsAndDepIds(
            String title,
            Integer thumb,
            String shortDesc,
            Integer isRequired,
            Integer isShow,
            Integer[] categoryIds,
            Integer[] depIds,
            Integer adminId);

    void updateWithCategoryIdsAndDepIds(
            Course course,
            String title,
            Integer thumb,
            String shortDesc,
            Integer isRequired,
            Integer isShow,
            String sortAt,
            Integer[] categoryIds,
            Integer[] depIds);

    void relateDepartments(Course course, Integer[] depIds);

    void resetRelateDepartments(Course course, Integer[] depIds);

    void relateCategories(Course course, Integer[] categoryIds);

    void resetRelateCategories(Course course, Integer[] categoryIds);

    Course findOrFail(Integer id) throws NotFoundException;

    List<Integer> getDepIdsByCourseId(Integer courseId);

    List<Integer> getCategoryIdsByCourseId(Integer courseId);

    void updateClassHour(Integer courseId, Integer classHour);

    void removeCategoryIdRelate(Integer categoryId);

    List<Course> chunks(List<Integer> ids, List<String> fields);

    List<Course> chunks(List<Integer> ids);

    List<Course> getOpenCoursesAndShow(Integer limit);

    List<Course> getOpenCoursesAndShow(Integer limit, List<Integer> categoryIds);

    List<Course> getDepCoursesAndShow(List<Integer> depIds);

    List<Course> getDepCoursesAndShow(List<Integer> depIds, List<Integer> categoryIds);

    Map<Integer, List<Integer>> getCategoryIdsGroup(List<Integer> courseIds);

    Map<Integer, List<Integer>> getDepIdsGroup(List<Integer> courseIds);

    Long total();
}
