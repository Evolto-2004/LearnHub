package xyz.learnhub.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.UserLearnCourseUpdateEvent;
import xyz.learnhub.course.service.UserLearnDurationRecordService;
import xyz.learnhub.course.service.UserLearnDurationStatsService;

/**
 *
 * @create 2023/3/22 14:18
 */
@Component
@Slf4j
public class UserLearnCourseUpdateListener {

    @Autowired private UserLearnDurationRecordService userLearnDurationRecordService;

    @Autowired private UserLearnDurationStatsService userLearnDurationStatsService;

    @EventListener
    public void storeLearnDuration(UserLearnCourseUpdateEvent event) {
        // 观看时长统计
        userLearnDurationStatsService.storeOrUpdate(
                event.getUserId(), event.getStartAt(), event.getEndAt());
        // 观看记录
        userLearnDurationRecordService.store(
                event.getUserId(),
                event.getCourseId() + "_" + event.getHourId(),
                "hour",
                event.getStartAt(),
                event.getEndAt());
    }
}
