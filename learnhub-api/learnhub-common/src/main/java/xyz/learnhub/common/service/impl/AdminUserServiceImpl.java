package xyz.learnhub.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.learnhub.common.domain.AdminUser;
import xyz.learnhub.common.domain.AdminUserRole;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.common.exception.ServiceException;
import xyz.learnhub.common.mapper.AdminUserMapper;
import xyz.learnhub.common.service.AdminUserRoleService;
import xyz.learnhub.common.service.AdminUserService;
import xyz.learnhub.common.types.paginate.AdminUserPaginateFilter;
import xyz.learnhub.common.types.paginate.PaginationResult;
import xyz.learnhub.common.util.HelperUtil;

@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser>
        implements AdminUserService {

    @Autowired private AdminUserRoleService userRoleService;

    public PaginationResult<AdminUser> paginate(
            int page, int size, AdminUserPaginateFilter filter) {
        QueryWrapper<AdminUser> wrapper = query().getWrapper().eq("1", "1");

        if (filter.getName() != null && filter.getName().trim().length() > 0) {
            wrapper.like("name", "%" + filter.getName() + "%");
        }
        if (filter.getRoleId() != null) {
            List<Integer> userIds = userRoleService.getAdminUserIds(filter.getRoleId());
            if (userIds == null || userIds.size() == 0) {
                userIds =
                        new ArrayList<>() {
                            {
                                add(0);
                            }
                        };
            }
            wrapper.in("id", userIds);
        }

        IPage<AdminUser> pageObj = new Page<>(page, size);
        pageObj = this.getBaseMapper().selectPage(pageObj, wrapper);

        PaginationResult<AdminUser> pageResult = new PaginationResult<>();
        pageResult.setData(pageObj.getRecords());
        pageResult.setTotal(pageObj.getTotal());

        return pageResult;
    }

    @Override
    public AdminUser findByEmail(String email) {
        return getOne(query().getWrapper().eq("email", email));
    }

    @Override
    public AdminUser findById(Integer id) {
        return getOne((query().getWrapper().eq("id", id)));
    }

    @Override
    public AdminUser findOrFail(Integer id) throws NotFoundException {
        AdminUser user = getOne(query().getWrapper().eq("id", id));
        if (user == null) {
            throw new NotFoundException("管理员不存在");
        }
        return user;
    }

    @Override
    public Boolean emailExists(String email) {
        AdminUser user = findByEmail(email);
        return user != null;
    }

    @Override
    @Transactional
    public void createWithRoleIds(
            String name, String email, String password, Integer isBanLogin, Integer[] roleIds)
            throws ServiceException {
        if (emailExists(email)) {
            throw new ServiceException("邮箱已存在");
        }

        String salt = HelperUtil.randomString(6);

        AdminUser adminUser = new AdminUser();
        adminUser.setName(name);
        adminUser.setEmail(email);
        adminUser.setSalt(salt);
        adminUser.setPassword(HelperUtil.MD5(password + salt));
        adminUser.setIsBanLogin(isBanLogin);
        adminUser.setCreatedAt(new Date());
        adminUser.setUpdatedAt(new Date());

        save(adminUser);

        relateRoles(adminUser, roleIds);
    }

    @Override
    public void relateRoles(AdminUser user, Integer[] roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            return;
        }
        List<AdminUserRole> userRoles = new ArrayList<>();
        for (int i = 0; i < roleIds.length; i++) {
            AdminUserRole userRole = new AdminUserRole();
            userRole.setAdminId(user.getId());
            userRole.setRoleId(roleIds[i]);
            userRoles.add(userRole);
        }
        userRoleService.saveBatch(userRoles);
    }

    @Override
    public void resetRelateRoles(AdminUser user, Integer[] roleIds) {
        removeRelateRolesByUserId(user.getId());
        relateRoles(user, roleIds);
    }

    @Override
    public List<Integer> getRoleIdsByUserId(Integer userId) {
        QueryWrapper<AdminUserRole> wrapper =
                userRoleService.query().getWrapper().eq("admin_id", userId);
        List<AdminUserRole> userRoles = userRoleService.list(wrapper);
        List<Integer> ids = new ArrayList<>();
        for (AdminUserRole userRole : userRoles) {
            ids.add(userRole.getRoleId());
        }
        return ids;
    }

    @Override
    @Transactional
    public void updateWithRoleIds(
            AdminUser user,
            String name,
            String email,
            String password,
            Integer isBanLogin,
            Integer[] roleIds)
            throws ServiceException {
        AdminUser updateAdminUser = new AdminUser();
        updateAdminUser.setId(user.getId());
        updateAdminUser.setName(name);
        updateAdminUser.setIsBanLogin(isBanLogin);

        if (!user.getEmail().equals(email)) { // 更换了邮箱
            if (emailExists(email)) {
                throw new ServiceException("邮箱已存在");
            }
            updateAdminUser.setEmail(email);
        }

        if (password != null && password.length() > 0) { // 更换了密码
            updateAdminUser.setPassword(HelperUtil.MD5(password + user.getSalt()));
        }

        updateById(updateAdminUser);

        resetRelateRoles(user, roleIds);
    }

    @Override
    @Transactional
    public void removeWithRoleIds(Integer userId) {
        removeRelateRolesByUserId(userId);
        removeById(userId);
    }

    @Override
    public void removeRelateRolesByUserId(Integer userId) {
        userRoleService.remove(userRoleService.query().getWrapper().eq("admin_id", userId));
    }

    @Override
    public void passwordChange(AdminUser user, String password) {
        String newPassword = HelperUtil.MD5(password + user.getSalt());
        AdminUser newUser = new AdminUser();
        newUser.setId(user.getId());
        newUser.setPassword(newPassword);
        updateById(newUser);
    }

    @Override
    public List<AdminUser> chunks(List<Integer> ids) {
        return list(query().getWrapper().in("id", ids));
    }

    @Override
    public Long total() {
        return count();
    }

    @Override
    public Map<Integer, List<Integer>> getAdminUserRoleIds(List<Integer> userIds) {
        Map<Integer, List<AdminUserRole>> records =
                userRoleService
                        .list(userRoleService.query().getWrapper().in("admin_id", userIds))
                        .stream()
                        .collect(Collectors.groupingBy(AdminUserRole::getAdminId));
        Map<Integer, List<Integer>> data = new HashMap<>();
        records.forEach(
                (adminId, record) -> {
                    data.put(adminId, record.stream().map(AdminUserRole::getRoleId).toList());
                });
        return data;
    }
}
