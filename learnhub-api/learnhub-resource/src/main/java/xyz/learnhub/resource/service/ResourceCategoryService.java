package xyz.learnhub.resource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import xyz.learnhub.resource.domain.ResourceCategory;

public interface ResourceCategoryService extends IService<ResourceCategory> {
    void rebuild(Integer resourceId, List<Integer> categoryIds);

    List<Integer> getRidsByCategoryId(Integer id);
}
