package xyz.learnhub.api.controller.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.learnhub.api.request.backend.AppConfigRequest;
import xyz.learnhub.common.annotation.BackendPermission;
import xyz.learnhub.common.annotation.Log;
import xyz.learnhub.common.constant.BPermissionConstant;
import xyz.learnhub.common.constant.BusinessTypeConstant;
import xyz.learnhub.common.constant.ConfigConstant;
import xyz.learnhub.common.constant.SystemConstant;
import xyz.learnhub.common.domain.AppConfig;
import xyz.learnhub.common.service.AppConfigService;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.common.util.StringUtil;
import xyz.learnhub.resource.service.ResourceService;

@RestController
@RequestMapping("/backend/v1/app-config")
public class AppConfigController {

    @Autowired private ResourceService resourceService;

    @Autowired private AppConfigService configService;

    @BackendPermission(slug = BPermissionConstant.SYSTEM_CONFIG)
    @GetMapping("")
    @Log(title = "系统配置-读取", businessType = BusinessTypeConstant.GET)
    public JsonResponse index() {
        List<AppConfig> configs = configService.allShow();
        List<AppConfig> appConfigList = new ArrayList<>();
        for (AppConfig item : configs) {
            if (item.getIsPrivate() == 1 && !item.getKeyValue().isBlank()) {
                item.setKeyValue(SystemConstant.CONFIG_MASK);
            }
            appConfigList.add(item);
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("app_config", appConfigList);

        // 获取签名url
        data.put(
                "resource_url",
                resourceService.chunksPreSignUrlByIds(configService.getAllImageValue()));
        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.SYSTEM_CONFIG)
    @PutMapping("")
    @Log(title = "系统配置-保存", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse save(@RequestBody AppConfigRequest req) {
        HashMap<String, String> data = req.getData();
        // 预览地址
        String s3Endpoint = data.get(ConfigConstant.S3_ENDPOINT);
        if (StringUtil.isNotEmpty(s3Endpoint)) {
            // 协议http:// https://
            if (s3Endpoint.length() < 7
                    || (!"http://".equalsIgnoreCase(s3Endpoint.substring(0, 7))
                            && !"https://".equalsIgnoreCase(s3Endpoint.substring(0, 8)))) {
                s3Endpoint = "https://" + s3Endpoint;
            }
            // 后缀
            if (s3Endpoint.endsWith("/")) {
                s3Endpoint = s3Endpoint.substring(0, s3Endpoint.length() - 1);
            }
            // 移除bucket
            String s3Bucket = data.get(ConfigConstant.S3_BUCKET);
            if (StringUtil.isNotEmpty(s3Bucket)) {
                String bucketDomain = s3Bucket + ".";
                String endpointLower = s3Endpoint.toLowerCase();
                String bucketDomainLower = bucketDomain.toLowerCase();
                if (endpointLower.contains(bucketDomainLower)) {
                    int index = endpointLower.indexOf(bucketDomainLower);
                    s3Endpoint =
                            s3Endpoint.substring(0, index)
                                    + s3Endpoint.substring(index + bucketDomain.length());
                }
            }
            data.put(ConfigConstant.S3_ENDPOINT, s3Endpoint);
        }

        HashMap<String, String> newConfig = new HashMap<>();
        data.forEach(
                (key, value) -> {
                    // 过滤掉未变动的private配置
                    if (SystemConstant.CONFIG_MASK.equals(value)) {
                        return;
                    }
                    String saveValue = value;

                    newConfig.put(key, saveValue);
                });
        configService.saveFromMap(newConfig);
        return JsonResponse.success();
    }
}
