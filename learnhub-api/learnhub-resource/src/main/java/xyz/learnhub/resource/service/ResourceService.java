package xyz.learnhub.resource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.common.types.paginate.PaginationResult;
import xyz.learnhub.common.types.paginate.ResourcePaginateFilter;
import xyz.learnhub.resource.domain.Resource;

public interface ResourceService extends IService<Resource> {

    PaginationResult<Resource> paginate(int page, int size, ResourcePaginateFilter filter);

    List<String> paginateType(ResourcePaginateFilter filter);

    Resource create(
            Integer adminId,
            String categoryIds,
            String type,
            String filename,
            String ext,
            Long size,
            String disk,
            String path,
            Integer parentId,
            Integer isHidden);

    void update(
            Resource resource,
            Integer adminId,
            String categoryIds,
            String type,
            String filename,
            String ext,
            Long size,
            String disk,
            String path,
            Integer parentId,
            Integer isHidden);

    Resource findOrFail(Integer id) throws NotFoundException;

    List<Resource> chunks(List<Integer> ids);

    List<Resource> chunks(List<Integer> ids, List<String> fields);

    Integer total(String type);

    Integer duration(Integer id);

    void updateNameAndCategoryId(Integer id, String name, Integer categoryId);

    List<Integer> categoryIds(Integer resourceId);

    Integer total(List<String> types);

    Map<Integer, String> chunksPreSignUrlByIds(List<Integer> ids);

    Map<Integer, String> downloadResById(Integer id);
}
