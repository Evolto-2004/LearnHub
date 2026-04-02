package xyz.learnhub.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.CourseCategoryDestroyEvent;
import xyz.learnhub.course.service.CourseService;

/**
 *
 * @create 2023/2/24 14:07
 */
@Component
public class CourseCategoryDestroyListener {

    @Autowired private CourseService courseService;

    @EventListener
    public void resetRelateCourseCategoryId(CourseCategoryDestroyEvent event) {
        courseService.removeCategoryIdRelate(event.getCategoryId());
    }
}
