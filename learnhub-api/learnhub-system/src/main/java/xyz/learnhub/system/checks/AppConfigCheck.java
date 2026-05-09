package xyz.learnhub.system.checks;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.learnhub.common.constant.BackendConstant;
import xyz.learnhub.common.constant.ConfigConstant;
import xyz.learnhub.common.domain.AppConfig;
import xyz.learnhub.common.service.AppConfigService;

@Component
@Order(100)
public class AppConfigCheck implements CommandLineRunner {

    private static final HashMap<String, AppConfig[]> configs =
            new HashMap<>() {
                {
                    // 系统配置
                    put(
                            "系统",
                            new AppConfig[] {
                                new AppConfig() {
                                    {
                                        setName("Logo");
                                        setSort(20);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_IMAGE);
                                        setKeyName(ConfigConstant.SYSTEM_LOGO);
                                        setKeyValue("");
                                    }
                                },
                            });
                    // 播放配置
                    put(
                            "播放配置",
                            new AppConfig[] {
                                new AppConfig() {
                                    {
                                        setName("播放器封面");
                                        setSort(10);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_IMAGE);
                                        setKeyName("player.poster");
                                        setKeyValue("");
                                        setHelp("播放器封面在学员观看视频时默认显示");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("禁止拖拽播放");
                                        setSort(60);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_SWITCH);
                                        setKeyName("player.disabled_drag");
                                        setKeyValue("0");
                                    }
                                },
                            });
                    put(
                            "学员配置",
                            new AppConfig[] {
                                new AppConfig() {
                                    {
                                        setName("默认头像");
                                        setSort(10);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_IMAGE);
                                        setKeyName(ConfigConstant.MEMBER_DEFAULT_AVATAR);
                                        setKeyValue("");
                                    }
                                },
                            });
                    put(
                            "S3存储",
                            new AppConfig[] {
                                new AppConfig() {
                                    {
                                        setName("AccessKey");
                                        setSort(10);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_TEXT);
                                        setKeyName(ConfigConstant.S3_ACCESS_KEY);
                                        setKeyValue("learnhub");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("SecretKey");
                                        setSort(20);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_TEXT);
                                        setKeyName(ConfigConstant.S3_SECRET_KEY);
                                        setKeyValue("learnhub123456");
                                        setIsPrivate(1);
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("Bucket");
                                        setSort(30);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_TEXT);
                                        setKeyName(ConfigConstant.S3_BUCKET);
                                        setKeyValue("learnhub");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("Region");
                                        setSort(35);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_TEXT);
                                        setKeyName(ConfigConstant.S3_REGION);
                                        setKeyValue("us-east-1");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("Endpoint");
                                        setSort(40);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_TEXT);
                                        setKeyName(ConfigConstant.S3_ENDPOINT);
                                        setKeyValue("http://minio.localhost:9000");
                                    }
                                },
                            });
                }
            };

    @Autowired private AppConfigService configService;

    @Override
    public void run(String... args) throws Exception {
        configService.remove(
                configService
                        .query()
                        .getWrapper()
                        .in("key_name", ConfigConstant.REMOVED_CONFIG_KEYS));

        Map<String, Long> keys = configService.allKeys();
        List<AppConfig> list = new ArrayList<>();
        Date now = new Date();

        configs.forEach(
                (groupNameValue, items) -> {
                    for (int i = 0; i < items.length; i++) {
                        AppConfig configItem = items[i];

                        if (keys.get(configItem.getKeyName()) != null) {
                            continue;
                        }

                        configItem.setGroupName(groupNameValue);
                        configItem.setCreatedAt(now);
                        list.add(configItem);
                    }
                });

        if (!list.isEmpty()) {
            configService.saveBatch(list);
        }
    }
}
