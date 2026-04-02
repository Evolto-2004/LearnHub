package xyz.learnhub.system.checks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.learnhub.common.domain.AdminLog;
import xyz.learnhub.common.constant.BackendConstant;
import xyz.learnhub.common.domain.AdminRole;
import xyz.learnhub.common.domain.AdminUser;
import xyz.learnhub.common.service.AdminLogService;
import xyz.learnhub.common.service.AdminRoleService;
import xyz.learnhub.common.service.AdminUserService;

@Component
@Slf4j
@Order(12)
public class SystemDataCheck implements CommandLineRunner {

    private static final String DEFAULT_SUPER_ADMIN_NAME = "Super Admin";
    private static final String LEGACY_SUPER_ADMIN_NAME = "超级管理员";
    private static final String DEFAULT_SUPER_ADMIN_EMAIL = "admin@learnhub.local";

    @Autowired private AdminRoleService adminRoleService;

    @Autowired private AdminUserService adminUserService;

    @Autowired private AdminLogService adminLogService;

    @Override
    public void run(String... args) throws Exception {
        adminInit();
    }

    private void adminInit() {
        try {
            normalizeDefaultAdminIdentity();

            AdminRole superRole = adminRoleService.getBySlug(BackendConstant.SUPER_ADMIN_ROLE);
            if (superRole != null) {
                return;
            }
            Integer roleId = adminRoleService.initSuperAdminRole();
            adminUserService.createWithRoleIds(
                    DEFAULT_SUPER_ADMIN_NAME,
                    DEFAULT_SUPER_ADMIN_EMAIL,
                    "learnhub",
                    0,
                    new Integer[] {roleId});
        } catch (Exception e) {
            log.error("Super Admin initialization failed: {}", e.getMessage());
        }
    }

    private void normalizeDefaultAdminIdentity() {
        AdminRole superRole = adminRoleService.getBySlug(BackendConstant.SUPER_ADMIN_ROLE);
        if (superRole != null && LEGACY_SUPER_ADMIN_NAME.equals(superRole.getName())) {
            AdminRole updatedRole = new AdminRole();
            updatedRole.setId(superRole.getId());
            updatedRole.setName(DEFAULT_SUPER_ADMIN_NAME);
            adminRoleService.updateById(updatedRole);
        }

        AdminUser defaultAdmin = adminUserService.findByEmail(DEFAULT_SUPER_ADMIN_EMAIL);
        if (defaultAdmin != null && LEGACY_SUPER_ADMIN_NAME.equals(defaultAdmin.getName())) {
            AdminUser updatedAdmin = new AdminUser();
            updatedAdmin.setId(defaultAdmin.getId());
            updatedAdmin.setName(DEFAULT_SUPER_ADMIN_NAME);
            adminUserService.updateById(updatedAdmin);
        }

        AdminLog updatedLog = new AdminLog();
        updatedLog.setAdminName(DEFAULT_SUPER_ADMIN_NAME);
        adminLogService.update(
                updatedLog,
                adminLogService.query().getWrapper().eq("admin_name", LEGACY_SUPER_ADMIN_NAME));
    }
}
