package xyz.learnhub.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.course.domain.CourseHour;

/**
 * @author tengteng
 * @description 针对表【course_hour】的数据库操作Service
 * @createDate 2023-03-15 10:16:45
 */
public interface CourseHourService extends IService<CourseHour> {

    CourseHour findOrFail(Integer id, Integer courseId) throws NotFoundException;

    void update(
            CourseHour courseHour, Integer chapterId, Integer sort, String title, Integer duration);

    List<CourseHour> getHoursByCourseId(Integer courseId);

    CourseHour create(
            Integer courseId,
            Integer chapterId,
            Integer sort,
            String title,
            String type,
            Integer rid,
            Integer duration);

    Integer getCountByCourseId(Integer courseId);

    Integer getCountByChapterId(Integer chapterId);

    void remove(Integer courseId, Integer chapterId);

    void updateSort(List<Integer> ids, Integer cid);

    List<Integer> getRidsByCourseId(Integer courseId, String type);

    List<CourseHour> chunk(List<Integer> hourIds);
}
