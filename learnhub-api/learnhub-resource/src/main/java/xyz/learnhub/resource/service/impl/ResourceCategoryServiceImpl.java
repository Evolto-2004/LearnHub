package xyz.learnhub.resource.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import xyz.learnhub.resource.domain.ResourceCategory;
import xyz.learnhub.resource.mapper.ResourceCategoryMapper;
import xyz.learnhub.resource.service.ResourceCategoryService;

@Service
public class ResourceCategoryServiceImpl
        extends ServiceImpl<ResourceCategoryMapper, ResourceCategory>
        implements ResourceCategoryService {
    @Override
    public void rebuild(Integer resourceId, List<Integer> categoryIds) {
        remove(query().getWrapper().eq("rid", resourceId));

        List<ResourceCategory> data = new ArrayList<>();
        categoryIds.forEach(
                categoryId -> {
                    data.add(
                            new ResourceCategory() {
                                {
                                    setCid(categoryId);
                                    setRid(resourceId);
                                }
                            });
                });

        saveBatch(data);
    }

    @Override
    public List<Integer> getRidsByCategoryId(Integer id) {
        return list(query().getWrapper().in("cid", id)).stream()
                .map(ResourceCategory::getRid)
                .toList();
    }
}
