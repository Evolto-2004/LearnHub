package xyz.learnhub.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.course.domain.CourseChapter;

/**
 * @author tengteng
 * @description 针对表【course_chapters】的数据库操作Service
 * @createDate 2023-02-26 17:30:19
 */
public interface CourseChapterService extends IService<CourseChapter> {

    List<CourseChapter> getChaptersByCourseId(Integer courseId);

    void create(Integer courseId, String name, Integer sort);

    void update(CourseChapter chapter, String name, Integer sort);

    CourseChapter findOrFail(Integer id) throws NotFoundException;

    CourseChapter findOrFail(Integer id, Integer courseId) throws NotFoundException;

    void updateSort(List<Integer> ids, Integer cid);
}
