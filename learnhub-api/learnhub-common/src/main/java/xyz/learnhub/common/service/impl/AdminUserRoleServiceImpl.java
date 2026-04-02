package xyz.learnhub.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import xyz.learnhub.common.domain.AdminUserRole;
import xyz.learnhub.common.mapper.AdminUserRoleMapper;
import xyz.learnhub.common.service.AdminUserRoleService;

/**
 * @author tengteng
 * @description 针对表【admin_user_role】的数据库操作Service实现
 * @createDate 2023-02-21 16:25:43
 */
@Service
public class AdminUserRoleServiceImpl extends ServiceImpl<AdminUserRoleMapper, AdminUserRole>
        implements AdminUserRoleService {
    @Override
    public List<Integer> getAdminUserIds(Integer roleId) {
        return list(query().getWrapper().eq("role_id", roleId)).stream()
                .map(AdminUserRole::getAdminId)
                .toList();
    }
}
