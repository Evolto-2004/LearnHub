package xyz.learnhub.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import xyz.learnhub.common.domain.AppConfig;
import xyz.learnhub.common.types.config.S3Config;

public interface AppConfigService extends IService<AppConfig> {

    Map<String, Long> allKeys();

    List<AppConfig> allShow();

    void saveFromMap(HashMap<String, String> data);

    Map<String, String> keyValues();

    S3Config getS3Config();

    List<Integer> getAllImageValue();

    Integer defaultAvatar();
}
