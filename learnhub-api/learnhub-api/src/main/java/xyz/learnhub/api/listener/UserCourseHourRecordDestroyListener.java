package xyz.learnhub.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.UserCourseHourRecordDestroyEvent;
import xyz.learnhub.course.service.UserCourseRecordService;

/**
 *
 * @create 2023/4/23 14:51
 */
@Component
@Slf4j
public class UserCourseHourRecordDestroyListener {

    @Autowired private UserCourseRecordService userCourseRecordService;

    @EventListener
    public void updateUserCourseRecord(UserCourseHourRecordDestroyEvent e) {
        userCourseRecordService.updateUserCourseLearnProgress(e.getUserId(), e.getCourseId(), 1);
    }
}
