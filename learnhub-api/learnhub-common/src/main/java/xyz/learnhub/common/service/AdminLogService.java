package xyz.learnhub.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.learnhub.common.domain.AdminLog;
import xyz.learnhub.common.types.paginate.AdminLogPaginateFiler;
import xyz.learnhub.common.types.paginate.PaginationResult;

/**
 * @author tengteng
 * @description 针对表【admin_logs】的数据库操作Service
 * @createDate 2023-02-17 15:40:31
 */
public interface AdminLogService extends IService<AdminLog> {
    PaginationResult<AdminLog> paginate(int page, int size, AdminLogPaginateFiler filter);

    AdminLog find(Integer id, Integer adminId);
}
