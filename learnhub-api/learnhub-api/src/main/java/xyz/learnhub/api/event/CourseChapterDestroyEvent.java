package xyz.learnhub.api.event;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @create 2023/2/26 17:42
 */
@Getter
@Setter
public class CourseChapterDestroyEvent extends ApplicationEvent {
    private Integer adminId;
    private Integer courseId;
    private Integer chapterId;
    private Date createdAt;

    public CourseChapterDestroyEvent(
            Object source, Integer adminId, Integer courseId, Integer chapterId) {
        super(source);
        this.adminId = adminId;
        this.courseId = courseId;
        this.chapterId = chapterId;
        this.createdAt = new Date();
    }
}
