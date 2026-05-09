package xyz.learnhub.api.controller.frontend;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.learnhub.common.constant.ConfigConstant;
import xyz.learnhub.common.service.AppConfigService;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.resource.service.ResourceService;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    @Autowired private AppConfigService appConfigService;

    @Autowired private ResourceService resourceService;

    @GetMapping("/config")
    public JsonResponse config() {
        Map<String, String> configs = appConfigService.keyValues();

        HashMap<String, Object> data = new HashMap<>();

        data.put("system-logo", configs.get(ConfigConstant.SYSTEM_LOGO));

        data.put("player-poster", configs.get("player.poster"));
        data.put("player-disabled-drag", configs.get("player.disabled_drag"));

        // 获取签名url
        data.put(
                "resource_url",
                resourceService.chunksPreSignUrlByIds(appConfigService.getAllImageValue()));

        return JsonResponse.data(data);
    }
}
