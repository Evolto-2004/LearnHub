package xyz.learnhub.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.CourseDestroyEvent;
import xyz.learnhub.course.service.CourseAttachmentService;
import xyz.learnhub.course.service.CourseCategoryService;
import xyz.learnhub.course.service.CourseDepartmentUserService;
import xyz.learnhub.course.service.UserCourseHourRecordService;
import xyz.learnhub.course.service.UserCourseRecordService;

/**
 *
 * @create 2023/2/24 17:19
 */
@Component
public class CourseDestroyListener {

    @Autowired private CourseDepartmentUserService courseDepartmentUserService;

    @Autowired private CourseCategoryService courseCategoryService;

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private CourseAttachmentService courseAttachmentService;

    @EventListener
    public void departmentRelateRemove(CourseDestroyEvent event) {
        courseDepartmentUserService.removeByCourseId(event.getCourseId());
    }

    @EventListener
    public void categoryRelateRemove(CourseDestroyEvent event) {
        courseCategoryService.removeByCourseId(event.getCourseId());
    }

    @EventListener
    public void attachmentRelateRemove(CourseDestroyEvent event) {
        courseAttachmentService.remove(event.getCourseId());
    }

    @EventListener
    public void removeUserRecords(CourseDestroyEvent event) {
        userCourseRecordService.removeByCourseId(event.getCourseId());
        userCourseHourRecordService.removeByCourseId(event.getCourseId());
    }
}
