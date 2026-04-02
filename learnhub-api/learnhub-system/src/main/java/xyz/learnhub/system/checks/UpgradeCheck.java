package xyz.learnhub.system.checks;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.learnhub.common.domain.AppConfig;
import xyz.learnhub.common.service.AdminPermissionService;
import xyz.learnhub.common.service.AppConfigService;

@Order(10000)
@Component
public class UpgradeCheck implements CommandLineRunner {

    @Autowired private AppConfigService appConfigService;

    @Autowired private AdminPermissionService permissionService;

    @Override
    public void run(String... args) throws Exception {
        upgrade_1_beta7();
        upgrade_1_4();
        upgrade_1_6();
    }

    private void upgrade_1_4() {
        appConfigService.remove(
                appConfigService
                        .query()
                        .getWrapper()
                        .in(
                                "key_name",
                                new ArrayList<>() {
                                    {
                                        add("ldap.user_dn_prefix");
                                    }
                                }));
    }

    private void upgrade_1_beta7() {
        appConfigService.update(
                new AppConfig() {
                    {
                        setIsPrivate(1);
                    }
                },
                appConfigService.query().getWrapper().eq("key_name", "minio.secret_key"));

        permissionService.remove(
                permissionService
                        .query()
                        .getWrapper()
                        .in(
                                "slug",
                                new ArrayList<>() {
                                    {
                                        add("resource-destroy");
                                    }
                                }));
    }

    private void upgrade_1_6() {
        permissionService.remove(
                permissionService
                        .query()
                        .getWrapper()
                        .in(
                                "slug",
                                new ArrayList<>() {
                                    {
                                        add("data-user-id-card");
                                    }
                                }));
    }
}
