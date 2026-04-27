package xyz.learnhub.api.controller.backend;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.learnhub.common.annotation.Log;
import xyz.learnhub.common.constant.BusinessTypeConstant;
import xyz.learnhub.common.constant.ConfigConstant;
import xyz.learnhub.common.context.BCtx;
import xyz.learnhub.common.service.AppConfigService;
import xyz.learnhub.common.service.CategoryService;
import xyz.learnhub.common.service.DepartmentService;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.common.util.StringUtil;
import xyz.learnhub.resource.service.ResourceService;

@RestController
@RequestMapping("/backend/v1/system")
@Slf4j
public class SystemController {

    @Autowired private DepartmentService departmentService;

    @Autowired private CategoryService categoryService;

    @Autowired private ResourceService resourceService;

    @Autowired private AppConfigService appConfigService;

    @GetMapping("/config")
    @Log(title = "其它-系统配置", businessType = BusinessTypeConstant.GET)
    public JsonResponse config() {
        Map<String, String> configData = BCtx.getConfig();

        HashMap<String, Object> data = new HashMap<>();

        data.put(ConfigConstant.SYSTEM_NAME, configData.get(ConfigConstant.SYSTEM_NAME));
        data.put(ConfigConstant.SYSTEM_LOGO, configData.get(ConfigConstant.SYSTEM_LOGO));
        data.put(ConfigConstant.SYSTEM_PC_URL, configData.get(ConfigConstant.SYSTEM_PC_URL));

        Integer rid = -1;
        String avatar = configData.get(ConfigConstant.MEMBER_DEFAULT_AVATAR);
        if (StringUtil.isNotEmpty(avatar)) {
            rid = Integer.parseInt(avatar);
        }

        data.put(ConfigConstant.MEMBER_DEFAULT_AVATAR, rid);

        // 获取签名url
        data.put(
                "resource_url",
                resourceService.chunksPreSignUrlByIds(appConfigService.getAllImageValue()));

        // 全部部门
        data.put("departments", departmentService.groupByParent());

        // 全部资源分类
        data.put("resource_categories", categoryService.groupByParent());

        return JsonResponse.data(data);
    }
}
