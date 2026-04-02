package xyz.learnhub.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.learnhub.common.domain.AdminLog;
import xyz.learnhub.common.mapper.AdminLogMapper;
import xyz.learnhub.common.service.AdminLogService;
import xyz.learnhub.common.types.paginate.AdminLogPaginateFiler;
import xyz.learnhub.common.types.paginate.PaginationResult;

/**
 * @author tengteng
 * @description 针对表【admin_logs】的数据库操作Service实现
 * @createDate 2023-02-17 15:40:31
 */
@Service
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog>
        implements AdminLogService {
    @Override
    public PaginationResult<AdminLog> paginate(int page, int size, AdminLogPaginateFiler filter) {
        filter.setPageStart((page - 1) * size);
        filter.setPageSize(size);

        PaginationResult<AdminLog> pageResult = new PaginationResult<>();
        pageResult.setData(getBaseMapper().paginate(filter));
        pageResult.setTotal(getBaseMapper().paginateCount(filter));

        return pageResult;
    }

    @Override
    public AdminLog find(Integer id, Integer adminId) {
        if (adminId == 0) {
            return getOne(query().getWrapper().eq("id", id));
        }
        return getOne(query().getWrapper().eq("id", id).eq("admin_id", adminId));
    }
}
