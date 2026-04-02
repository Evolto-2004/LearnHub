package xyz.learnhub.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import xyz.learnhub.course.domain.CourseCategory;
import xyz.learnhub.course.mapper.CourseCategoryMapper;
import xyz.learnhub.course.service.CourseCategoryService;

/**
 * @author tengteng
 * @description 针对表【resource_course_category】的数据库操作Service实现
 * @createDate 2023-03-09 09:54:22
 */
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory>
        implements CourseCategoryService {

    @Override
    public List<Integer> getCourseIdsByCategoryIds(List<Integer> categoryIds) {
        return list(query().getWrapper().in("category_id", categoryIds)).stream()
                .map(CourseCategory::getCourseId)
                .toList();
    }

    @Override
    public void removeByCourseId(Integer id) {
        remove(query().getWrapper().eq("course_id", id));
    }

    @Override
    public void removeByCategoryId(Integer id) {
        remove(query().getWrapper().eq("category_id", id));
    }

    @Override
    public List<Integer> getCategoryIdsByCourseId(Integer courseId) {
        return list(query().getWrapper().eq("course_id", courseId)).stream()
                .map(CourseCategory::getCategoryId)
                .toList();
    }

    @Override
    public List<Integer> getCourseIdsByCategoryId(Integer id) {
        return list(query().getWrapper().eq("category_id", id)).stream()
                .map(CourseCategory::getCourseId)
                .toList();
    }
}
