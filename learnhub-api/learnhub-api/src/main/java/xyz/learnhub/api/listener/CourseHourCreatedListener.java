package xyz.learnhub.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.CourseHourCreatedEvent;
import xyz.learnhub.course.service.CourseHourService;
import xyz.learnhub.course.service.CourseService;

/**
 *
 * @create 2023/2/26 18:22
 */
@Component
public class CourseHourCreatedListener {

    @Autowired private CourseHourService hourService;

    @Autowired private CourseService courseService;

    @EventListener
    public void courseClassHourUpdate(CourseHourCreatedEvent event) {
        Integer classHour = hourService.getCountByCourseId(event.getCourseId());
        courseService.updateClassHour(event.getCourseId(), classHour);
    }
}
