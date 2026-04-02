package xyz.learnhub.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.resource.domain.ResourceExtra;

/**
 * @author tengteng
 * @description 针对表【resource_videos】的数据库操作Mapper
 * @createDate 2023-03-08 13:39:06
 */
@Mapper
public interface ResourceExtraMapper extends BaseMapper<ResourceExtra> {}
