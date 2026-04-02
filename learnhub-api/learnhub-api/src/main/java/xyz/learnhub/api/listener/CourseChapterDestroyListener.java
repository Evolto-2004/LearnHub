package xyz.learnhub.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.CourseChapterDestroyEvent;
import xyz.learnhub.course.service.CourseHourService;

/**
 *
 * @create 2023/2/26 18:25
 */
@Component
public class CourseChapterDestroyListener {

    @Autowired private CourseHourService hourService;

    @EventListener
    public void resetCourseHourChapterId(CourseChapterDestroyEvent event) {
        hourService.remove(event.getCourseId(), event.getChapterId());
    }
}
