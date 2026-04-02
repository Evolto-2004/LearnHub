package xyz.learnhub.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.learnhub.common.domain.AdminRolePermission;
import xyz.learnhub.common.mapper.AdminRolePermissionMapper;
import xyz.learnhub.common.service.AdminRolePermissionService;

/**
 * @author tengteng
 * @description 针对表【admin_role_permission】的数据库操作Service实现
 * @createDate 2023-02-21 16:07:01
 */
@Service
public class AdminRolePermissionServiceImpl
        extends ServiceImpl<AdminRolePermissionMapper, AdminRolePermission>
        implements AdminRolePermissionService {}
