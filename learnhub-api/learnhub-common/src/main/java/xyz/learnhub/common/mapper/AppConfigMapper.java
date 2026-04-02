package xyz.learnhub.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.domain.AppConfig;

/**
 * @author tengteng
 * @description 针对表【app_config】的数据库操作Mapper
 * @createDate 2023-03-09 13:55:39
 */
@Mapper
public interface AppConfigMapper extends BaseMapper<AppConfig> {}
