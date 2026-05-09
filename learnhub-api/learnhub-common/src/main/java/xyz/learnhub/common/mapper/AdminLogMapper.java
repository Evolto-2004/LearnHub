package xyz.learnhub.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.domain.AdminLog;

/**
 * @author tengteng
 * @description 针对表【admin_logs】的数据库操作Mapper
 * @createDate 2023-02-17 15:40:31
 */
@Mapper
public interface AdminLogMapper extends BaseMapper<AdminLog> {}
