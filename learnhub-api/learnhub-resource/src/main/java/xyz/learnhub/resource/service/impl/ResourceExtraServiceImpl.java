package xyz.learnhub.resource.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import xyz.learnhub.resource.domain.ResourceExtra;
import xyz.learnhub.resource.mapper.ResourceExtraMapper;
import xyz.learnhub.resource.service.ResourceExtraService;

/**
 * @author tengteng
 * @description 针对表【resource_videos】的数据库操作Service实现
 * @createDate 2023-03-02 15:13:03
 */
@Service
public class ResourceExtraServiceImpl extends ServiceImpl<ResourceExtraMapper, ResourceExtra>
        implements ResourceExtraService {
    @Override
    public void create(Integer resourceId, Integer duration, Integer poster) {
        ResourceExtra video = new ResourceExtra();
        video.setRid(resourceId);
        video.setDuration(duration);
        video.setPoster(poster);
        video.setCreatedAt(new Date());
        save(video);
    }

    @Override
    public void removeByRid(Integer resourceId) {
        remove(query().getWrapper().eq("rid", resourceId));
    }

    @Override
    public List<ResourceExtra> chunksByRids(List<Integer> resourceIds) {
        if (resourceIds == null || resourceIds.size() == 0) {
            return new ArrayList<>();
        }
        return list(query().getWrapper().in("rid", resourceIds));
    }
}
