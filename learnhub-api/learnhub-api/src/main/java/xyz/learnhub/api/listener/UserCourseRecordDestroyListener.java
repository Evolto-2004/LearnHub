package xyz.learnhub.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.UserCourseRecordDestroyEvent;
import xyz.learnhub.course.service.UserCourseHourRecordService;

/**
 *
 * @create 2023/4/4 10:16
 */
@Component
public class UserCourseRecordDestroyListener {

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @EventListener
    public void emptyUserCourseHourRecords(UserCourseRecordDestroyEvent event) {
        userCourseHourRecordService.remove(event.getUserId(), event.getCourseId());
    }
}
