package xyz.learnhub.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.domain.AdminRolePermission;

/**
 * @author tengteng
 * @description 针对表【admin_role_permission】的数据库操作Mapper
 * @createDate 2023-02-21 16:07:01
 */
@Mapper
public interface AdminRolePermissionMapper extends BaseMapper<AdminRolePermission> {}
