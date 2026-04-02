package xyz.learnhub.api.controller.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.learnhub.common.annotation.Log;
import xyz.learnhub.common.constant.BusinessTypeConstant;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.common.util.MemoryCacheUtil;

@RestController
@Slf4j
@RequestMapping("/backend/v1/cache")
public class CacheController {

    @Autowired private MemoryCacheUtil memoryCacheUtil;

    @GetMapping("/list")
    @Log(title = "缓存列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse list() {
        Map<String, Object> data = new HashMap<>();
        data.put("keys", memoryCacheUtil.getAllKeys());
        data.put("cache", memoryCacheUtil.getAllCache());
        return JsonResponse.data(data);
    }

    @DeleteMapping("/clear")
    @Log(title = "缓存删除key", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse clear(@RequestParam HashMap<String, Object> params) {
        String cache_key = MapUtils.getString(params, "cache_key");
        memoryCacheUtil.del(cache_key);
        return JsonResponse.success();
    }

    @DeleteMapping("/clear/all")
    @Log(title = "缓存清空", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse clearAll() {
        List<String> keys = memoryCacheUtil.getAllKeys();
        for (String key : keys) {
            MemoryCacheUtil.del(key);
        }
        return JsonResponse.success();
    }
}
