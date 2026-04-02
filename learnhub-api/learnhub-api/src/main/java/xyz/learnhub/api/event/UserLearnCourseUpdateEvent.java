package xyz.learnhub.api.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @create 2023/3/22 14:14
 */
@Setter
@Getter
public class UserLearnCourseUpdateEvent extends ApplicationEvent {

    private Integer userId;
    private Integer courseId;
    private Integer hourId;
    private Long startAt;
    private Long endAt;

    public UserLearnCourseUpdateEvent(
            Object source,
            Integer userId,
            Integer courseId,
            Integer hourId,
            Long startTime,
            Long endTime) {
        super(source);
        this.userId = userId;
        this.courseId = courseId;
        this.hourId = hourId;
        this.startAt = startTime;
        this.endAt = endTime;
    }
}
