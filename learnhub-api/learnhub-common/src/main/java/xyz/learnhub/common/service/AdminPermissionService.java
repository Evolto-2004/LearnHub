package xyz.learnhub.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.HashMap;
import java.util.List;
import xyz.learnhub.common.domain.AdminPermission;

/**
 * @author tengteng
 * @description 针对表【admin_permissions】的数据库操作Service
 * @createDate 2023-02-20 14:27:50
 */
public interface AdminPermissionService extends IService<AdminPermission> {

    HashMap<String, Integer> allSlugs();

    List<AdminPermission> listOrderBySortAsc();

    HashMap<String, Boolean> getSlugsByIds(List<Integer> ids);

    List<Integer> allIds();

    List<AdminPermission> chunks(List<Integer> ids);
}
