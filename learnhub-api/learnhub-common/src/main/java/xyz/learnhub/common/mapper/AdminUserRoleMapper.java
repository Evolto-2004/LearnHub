package xyz.learnhub.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.domain.AdminUserRole;

/**
 * @author tengteng
 * @description 针对表【admin_user_role】的数据库操作Mapper
 * @createDate 2023-02-21 16:25:43
 */
@Mapper
public interface AdminUserRoleMapper extends BaseMapper<AdminUserRole> {}
