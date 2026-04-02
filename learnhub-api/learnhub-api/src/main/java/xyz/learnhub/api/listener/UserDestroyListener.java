package xyz.learnhub.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.UserDestroyEvent;
import xyz.learnhub.common.service.UserLoginRecordService;
import xyz.learnhub.common.service.UserService;
import xyz.learnhub.course.service.UserCourseHourRecordService;
import xyz.learnhub.course.service.UserCourseRecordService;
import xyz.learnhub.course.service.UserLearnDurationRecordService;
import xyz.learnhub.course.service.UserLearnDurationStatsService;

/**
 *
 * @create 2023/2/23 15:18
 */
@Component
@Slf4j
public class UserDestroyListener {

    @Autowired private UserService userService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserLearnDurationRecordService userLearnDurationRecordService;

    @Autowired private UserLearnDurationStatsService userLearnDurationStatsService;

    @Autowired private UserLoginRecordService userLoginRecordService;

    @EventListener
    public void remoteRelation(UserDestroyEvent event) {
        userService.removeRelateDepartmentsByUserId(event.getUserId());
        userCourseHourRecordService.remove(event.getUserId());
        userCourseRecordService.destroy(event.getUserId());
        userLearnDurationRecordService.remove(event.getUserId());
        userLearnDurationStatsService.remove(event.getUserId());
        userLoginRecordService.remove(event.getUserId());
    }
}
