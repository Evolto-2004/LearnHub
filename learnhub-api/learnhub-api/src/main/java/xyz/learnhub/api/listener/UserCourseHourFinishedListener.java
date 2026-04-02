package xyz.learnhub.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.UserCourseHourFinishedEvent;
import xyz.learnhub.course.service.CourseHourService;
import xyz.learnhub.course.service.UserCourseHourRecordService;
import xyz.learnhub.course.service.UserCourseRecordService;

/**
 *
 * @create 2023/3/20 17:41
 */
@Component
public class UserCourseHourFinishedListener {

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private CourseHourService hourService;

    @EventListener
    public void userCourseProgressUpdate(UserCourseHourFinishedEvent evt) {
        Integer hourCount = hourService.getCountByCourseId(evt.getCourseId());
        Integer finishedCount =
                userCourseHourRecordService.getFinishedHourCount(
                        evt.getUserId(), evt.getCourseId());
        userCourseRecordService.storeOrUpdate(
                evt.getUserId(), evt.getCourseId(), hourCount, finishedCount);
    }
}
