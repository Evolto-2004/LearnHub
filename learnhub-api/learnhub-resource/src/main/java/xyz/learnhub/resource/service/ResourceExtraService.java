package xyz.learnhub.resource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import xyz.learnhub.resource.domain.ResourceExtra;

/**
 * @author tengteng
 * @description 针对表【resource_videos】的数据库操作Service
 * @createDate 2023-03-02 15:13:03
 */
public interface ResourceExtraService extends IService<ResourceExtra> {

    void create(Integer resourceId, Integer duration, Integer poster);

    void removeByRid(Integer resourceId);

    List<ResourceExtra> chunksByRids(List<Integer> resourceIds);
}
