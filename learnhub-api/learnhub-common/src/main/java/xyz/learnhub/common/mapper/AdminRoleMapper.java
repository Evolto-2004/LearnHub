package xyz.learnhub.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.domain.AdminRole;

/**
 * @author tengteng
 * @description 针对表【admin_roles】的数据库操作Mapper
 * @createDate 2023-02-21 15:53:27
 */
@Mapper
public interface AdminRoleMapper extends BaseMapper<AdminRole> {}
