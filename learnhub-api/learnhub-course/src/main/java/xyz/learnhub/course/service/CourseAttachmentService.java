package xyz.learnhub.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.course.domain.CourseAttachment;

public interface CourseAttachmentService extends IService<CourseAttachment> {

    CourseAttachment findOrFail(Integer id, Integer courseId) throws NotFoundException;

    void update(CourseAttachment courseAttachment, Integer sort, String title);

    List<CourseAttachment> getAttachmentsByCourseId(Integer courseId);

    CourseAttachment create(Integer courseId, Integer sort, String title, String type, Integer rid);

    Integer getCountByCourseId(Integer courseId);

    void remove(Integer courseId);

    void updateSort(List<Integer> ids, Integer cid);

    List<Integer> getRidsByCourseId(Integer courseId);

    List<CourseAttachment> chunk(List<Integer> attachmentIds);
}
