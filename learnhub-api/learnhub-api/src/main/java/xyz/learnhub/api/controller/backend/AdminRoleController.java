package xyz.learnhub.api.controller.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.learnhub.api.request.backend.AdminRoleRequest;
import xyz.learnhub.common.annotation.BackendPermission;
import xyz.learnhub.common.annotation.Log;
import xyz.learnhub.common.constant.BPermissionConstant;
import xyz.learnhub.common.constant.BackendConstant;
import xyz.learnhub.common.constant.BusinessTypeConstant;
import xyz.learnhub.common.domain.AdminPermission;
import xyz.learnhub.common.domain.AdminRole;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.common.service.AdminPermissionService;
import xyz.learnhub.common.service.AdminRoleService;
import xyz.learnhub.common.types.JsonResponse;

@RestController
@RequestMapping("/backend/v1/admin-role")
@Slf4j
public class AdminRoleController {

    @Autowired private AdminRoleService roleService;

    @Autowired private AdminPermissionService permissionService;

    @GetMapping("/index")
    @Log(title = "管理员角色-列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse index() {
        List<AdminRole> data = roleService.list();
        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.ADMIN_ROLE)
    @GetMapping("/create")
    @Log(title = "管理员角色-新建", businessType = BusinessTypeConstant.GET)
    public JsonResponse create() {
        List<AdminPermission> permissions = permissionService.listOrderBySortAsc();

        HashMap<String, Object> data = new HashMap<>();
        data.put(
                "perm_action",
                permissions.stream().collect(Collectors.groupingBy(AdminPermission::getType)));

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.ADMIN_ROLE)
    @PostMapping("/create")
    @Log(title = "管理员角色-新建", businessType = BusinessTypeConstant.INSERT)
    public JsonResponse store(@RequestBody @Validated AdminRoleRequest request) {
        roleService.createWithPermissionIds(request.getName(), request.getPermissionIds());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.ADMIN_ROLE)
    @GetMapping("/{id}")
    @Log(title = "管理员角色-编辑", businessType = BusinessTypeConstant.GET)
    public JsonResponse edit(@PathVariable(name = "id") Integer id) throws NotFoundException {
        AdminRole role = roleService.findOrFail(id);

        // 关联的权限
        List<Integer> permissionIds = roleService.getPermissionIdsByRoleId(role.getId());
        List<Integer> permAction = new ArrayList<>();
        List<Integer> permData = new ArrayList<>();
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<AdminPermission> permissions = permissionService.chunks(permissionIds);
            Map<String, List<AdminPermission>> permissionsGroup =
                    permissions.stream().collect(Collectors.groupingBy(AdminPermission::getType));
            if (permissionsGroup.get("action") != null) {
                permAction =
                        permissionsGroup.get("action").stream()
                                .map(AdminPermission::getId)
                                .toList();
            }
            if (permissionsGroup.get("data") != null) {
                permData =
                        permissionsGroup.get("data").stream().map(AdminPermission::getId).toList();
            }
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("role", role);
        data.put("perm_action", permAction);
        data.put("perm_data", permData);

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.ADMIN_ROLE)
    @PutMapping("/{id}")
    @Log(title = "管理员角色-编辑", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse update(
            @PathVariable(name = "id") Integer id, @RequestBody @Validated AdminRoleRequest request)
            throws NotFoundException {
        AdminRole role = roleService.findOrFail(id);
        if (role.getSlug().equals(BackendConstant.SUPER_ADMIN_ROLE)) {
            return JsonResponse.error("超级管理权限无法编辑");
        }

        roleService.updateWithPermissionIds(role, request.getName(), request.getPermissionIds());

        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.ADMIN_ROLE)
    @DeleteMapping("/{id}")
    @Log(title = "管理员角色-删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) throws NotFoundException {
        AdminRole role = roleService.findOrFail(id);

        if (role.getSlug().equals(BackendConstant.SUPER_ADMIN_ROLE)) {
            return JsonResponse.error("超级管理角色无法删除");
        }

        roleService.removeWithPermissions(role);

        return JsonResponse.success();
    }
}
