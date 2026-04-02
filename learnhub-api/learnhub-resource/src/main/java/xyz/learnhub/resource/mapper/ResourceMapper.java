package xyz.learnhub.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.types.paginate.ResourcePaginateFilter;
import xyz.learnhub.resource.domain.Resource;

/**
 * @author tengteng
 * @description 针对表【resource】的数据库操作Mapper
 * @createDate 2023-03-13 10:25:30
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Resource> paginate(ResourcePaginateFilter filter);

    Long paginateCount(ResourcePaginateFilter filter);

    List<String> paginateType(ResourcePaginateFilter filter);
}
