package xyz.learnhub.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import xyz.learnhub.course.domain.CourseCategory;

/**
 * @author tengteng
 * @description 针对表【resource_course_category】的数据库操作Service
 * @createDate 2023-03-09 09:54:22
 */
public interface CourseCategoryService extends IService<CourseCategory> {

    List<Integer> getCourseIdsByCategoryIds(List<Integer> categoryIds);

    void removeByCourseId(Integer id);

    void removeByCategoryId(Integer id);

    List<Integer> getCategoryIdsByCourseId(Integer courseId);

    List<Integer> getCourseIdsByCategoryId(Integer id);
}
