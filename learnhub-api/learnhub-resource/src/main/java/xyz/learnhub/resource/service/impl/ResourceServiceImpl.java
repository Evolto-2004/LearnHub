package xyz.learnhub.resource.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.common.service.AppConfigService;
import xyz.learnhub.common.types.paginate.PaginationResult;
import xyz.learnhub.common.types.paginate.ResourcePaginateFilter;
import xyz.learnhub.common.util.S3Util;
import xyz.learnhub.common.util.StringUtil;
import xyz.learnhub.resource.domain.Resource;
import xyz.learnhub.resource.domain.ResourceExtra;
import xyz.learnhub.resource.mapper.ResourceMapper;
import xyz.learnhub.resource.service.ResourceExtraService;
import xyz.learnhub.resource.service.ResourceService;

/**
 * @author tengteng
 * @description 针对表【resource】的数据库操作Service实现
 * @createDate 2023-02-23 10:50:26
 */
@Service
@Slf4j
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
        implements ResourceService {

    @Autowired private ResourceExtraService resourceExtraService;

    @Autowired private AppConfigService appConfigService;

    @Override
    public PaginationResult<Resource> paginate(int page, int size, ResourcePaginateFilter filter) {
        PaginationResult<Resource> pageResult = new PaginationResult<>();

        filter.setPageStart((page - 1) * size);
        filter.setPageSize(size);

        pageResult.setData(getBaseMapper().paginate(filter));
        pageResult.setTotal(getBaseMapper().paginateCount(filter));

        return pageResult;
    }

    @Override
    public List<String> paginateType(ResourcePaginateFilter filter) {
        return getBaseMapper().paginateType(filter);
    }

    @Override
    @Transactional
    public Resource create(
            Integer adminId,
            String type,
            String filename,
            String ext,
            Long size,
            String disk,
            String path,
            Integer parentId,
            Integer isHidden) {
        Resource resource = new Resource();
        resource.setAdminId(adminId);
        resource.setType(type);
        resource.setName(filename);
        resource.setExtension(ext);
        resource.setSize(size);
        resource.setDisk(disk);
        resource.setPath(path);
        resource.setCreatedAt(new Date());
        resource.setParentId(parentId);
        resource.setIsHidden(isHidden);
        save(resource);
        return resource;
    }

    @Override
    @Transactional
    public void update(
            Resource resource,
            Integer adminId,
            String type,
            String filename,
            String ext,
            Long size,
            String disk,
            String path,
            Integer parentId,
            Integer isHidden) {
        resource.setAdminId(adminId);
        resource.setType(type);
        resource.setName(filename);
        resource.setExtension(ext);
        resource.setSize(size);
        resource.setDisk(disk);
        resource.setPath(path);
        resource.setCreatedAt(new Date());
        resource.setParentId(parentId);
        resource.setIsHidden(isHidden);
        updateById(resource);
    }

    @Override
    public Resource findOrFail(Integer id) throws NotFoundException {
        Resource resource = getById(id);
        if (resource == null) {
            throw new NotFoundException("资源不存在");
        }
        return resource;
    }

    @Override
    public List<Resource> chunks(List<Integer> ids) {
        return list(query().getWrapper().in("id", ids));
    }

    @Override
    public List<Resource> chunks(List<Integer> ids, List<String> fields) {
        return list(query().getWrapper().in("id", ids).select(fields));
    }

    @Override
    public Integer total(String type) {
        return Math.toIntExact(count(query().getWrapper().eq("type", type).eq("is_hidden", 0)));
    }

    @Override
    public Integer duration(Integer id) {
        ResourceExtra resourceExtra =
                resourceExtraService.getOne(
                        resourceExtraService.query().getWrapper().eq("rid", id));
        if (resourceExtra == null) {
            return null;
        }
        return resourceExtra.getDuration();
    }

    @Override
    @Transactional
    public void updateName(Integer id, String name) {
        Resource resource = new Resource();
        resource.setId(id);
        resource.setName(name);
        updateById(resource);
    }

    @Override
    public Integer total(List<String> types) {
        return Math.toIntExact(count(query().getWrapper().in("type", types).eq("is_hidden", 0)));
    }

    @Override
    public Map<Integer, String> chunksPreSignUrlByIds(List<Integer> ids) {
        if (StringUtil.isEmpty(ids)) {
            return new HashMap<>();
        }

        S3Util s3Util = new S3Util(appConfigService.getS3Config());
        Map<Integer, String> preSignUrlMap = new HashMap<>();
        List<Resource> resourceList = list(query().getWrapper().in("id", ids));
        if (StringUtil.isNotEmpty(resourceList)) {
            resourceList.forEach(
                    resource -> {
                        String path = resource.getPath();
                        try {
                            String url = s3Util.generateEndpointPreSignUrl(path, "");
                            if (StringUtil.isNotEmpty(url)) {
                                preSignUrlMap.put(resource.getId(), url);
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    });
        }
        return preSignUrlMap;
    }

    @Override
    public Map<Integer, String> downloadResById(Integer id) {
        Resource resource = getById(id);
        Map<Integer, String> preSignUrlMap = new HashMap<>();
        try {
            S3Util s3Util = new S3Util(appConfigService.getS3Config());
            String url = s3Util.generateEndpointPreSignUrl(resource.getPath(), resource.getName());
            if (StringUtil.isNotEmpty(url)) {
                preSignUrlMap.put(resource.getId(), url);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return preSignUrlMap;
    }
}
