package xyz.learnhub.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.resource.domain.ResourceCategory;

/**
 * @author tengteng
 * @description 针对表【resource_category】的数据库操作Mapper
 * @createDate 2023-03-08 16:54:56
 */
@Mapper
public interface ResourceCategoryMapper extends BaseMapper<ResourceCategory> {}
