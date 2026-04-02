package xyz.learnhub.api.event;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @create 2023/2/24 14:31
 */
@Getter
@Setter
public class CourseDestroyEvent extends ApplicationEvent {

    private Integer courseId;
    private Date createdAt;
    private Integer adminId;

    public CourseDestroyEvent(Object source, Integer adminId, Integer courseId) {
        super(source);
        this.courseId = courseId;
        this.createdAt = new Date();
        this.adminId = adminId;
    }
}
