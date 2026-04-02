package xyz.learnhub.api.event;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @create 2023/2/26 17:52
 */
@Getter
@Setter
public class CourseHourDestroyEvent extends ApplicationEvent {
    private Integer adminId;
    private Integer hourId;
    private Integer courseId;
    private Integer chapterId;
    private Date createdAt;

    public CourseHourDestroyEvent(
            Object source, Integer adminId, Integer courseId, Integer chapterId, Integer hourId) {
        super(source);
        this.adminId = adminId;
        this.courseId = courseId;
        this.chapterId = chapterId;
        this.hourId = hourId;
        this.createdAt = new Date();
    }
}
