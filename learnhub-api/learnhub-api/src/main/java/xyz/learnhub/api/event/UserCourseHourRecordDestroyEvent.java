package xyz.learnhub.api.event;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @create 2023/4/23 14:48
 */
@Getter
@Setter
public class UserCourseHourRecordDestroyEvent extends ApplicationEvent {

    private Integer userId;
    private Integer courseId;
    private Integer hourId;
    private Date at;

    public UserCourseHourRecordDestroyEvent(
            Object source, Integer userId, Integer courseId, Integer hourId) {
        super(source);
        this.userId = userId;
        this.courseId = courseId;
        this.hourId = hourId;
        this.at = new Date();
    }
}
