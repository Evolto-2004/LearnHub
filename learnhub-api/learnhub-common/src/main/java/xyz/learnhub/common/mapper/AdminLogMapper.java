package xyz.learnhub.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.domain.AdminLog;
import xyz.learnhub.common.types.paginate.AdminLogPaginateFiler;

/**
 * @author tengteng
 * @description 针对表【admin_logs】的数据库操作Mapper
 * @createDate 2023-02-17 15:40:31
 */
@Mapper
public interface AdminLogMapper extends BaseMapper<AdminLog> {
    List<AdminLog> paginate(AdminLogPaginateFiler filer);

    Long paginateCount(AdminLogPaginateFiler filer);
}
