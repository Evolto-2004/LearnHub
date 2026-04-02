package xyz.learnhub.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.UserLogoutEvent;
import xyz.learnhub.common.service.UserLoginRecordService;

/**
 *
 * @create 2023/3/21 14:51
 */
@Component
@Slf4j
public class UserLogoutListener {

    @Autowired private UserLoginRecordService userLoginRecordService;

    @Async
    @EventListener
    public void updateLoginRecord(UserLogoutEvent event) {
        userLoginRecordService.logout(event.getUserId(), event.getJti());
    }
}
