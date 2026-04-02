package xyz.learnhub.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.learnhub.api.event.DepartmentDestroyEvent;
import xyz.learnhub.common.service.DepartmentService;

/**
 *
 * @create 2023/2/23 15:28
 */
@Component
public class DepartmentDestroyListener {
    @Autowired private DepartmentService departmentService;

    @EventListener
    public void updateLoginInfo(DepartmentDestroyEvent event) {
        departmentService.remoteRelateUsersByDepId(event.getDepId());
    }
}
