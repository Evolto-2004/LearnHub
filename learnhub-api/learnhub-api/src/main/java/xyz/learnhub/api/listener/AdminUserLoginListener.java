package xyz.learnhub.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.AdminUserLoginEvent;
import xyz.learnhub.common.domain.AdminUser;
import xyz.learnhub.common.service.AdminUserService;

@Component
@Slf4j
public class AdminUserLoginListener {

    @Autowired private AdminUserService adminUserService;

    @EventListener
    public void updateLoginInfo(AdminUserLoginEvent event) {
        AdminUser adminUser = new AdminUser();

        adminUser.setId(event.getAdminId());
        adminUser.setLoginAt(event.getLoginAt());
        adminUser.setLoginTimes(event.getLoginTimes() + 1);
        adminUser.setLoginIp(event.getIp());

        adminUserService.updateById(adminUser);
    }
}
