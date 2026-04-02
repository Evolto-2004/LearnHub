package xyz.learnhub.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import xyz.learnhub.common.domain.AdminRole;
import xyz.learnhub.common.exception.NotFoundException;

/**
 * @author tengteng
 * @description 针对表【admin_roles】的数据库操作Service
 * @createDate 2023-02-21 15:53:27
 */
public interface AdminRoleService extends IService<AdminRole> {

    AdminRole getBySlug(String slug);

    Integer initSuperAdminRole();

    void createWithPermissionIds(String name, Integer[] permissionIds);

    void relatePermissions(AdminRole role, Integer[] permissionIds);

    void resetRelatePermissions(AdminRole role, Integer[] permissionIds);

    void updateWithPermissionIds(AdminRole role, String name, Integer[] permissionIds);

    AdminRole findOrFail(Integer id) throws NotFoundException;

    void removeWithPermissions(AdminRole role);

    List<Integer> getPermissionIdsByRoleId(Integer roleId);

    List<Integer> getPermissionIdsByRoleIds(List<Integer> roleIds);

    void removeRelatePermissionByRoleId(Integer roleId);
}
