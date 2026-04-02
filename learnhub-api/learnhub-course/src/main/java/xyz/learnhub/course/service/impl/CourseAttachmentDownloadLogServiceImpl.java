package xyz.learnhub.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.learnhub.common.types.paginate.CourseAttachmentDownloadLogPaginateFiler;
import xyz.learnhub.common.types.paginate.PaginationResult;
import xyz.learnhub.course.domain.CourseAttachmentDownloadLog;
import xyz.learnhub.course.mapper.CourseAttachmentDownloadLogMapper;
import xyz.learnhub.course.service.CourseAttachmentDownloadLogService;

@Service
public class CourseAttachmentDownloadLogServiceImpl
        extends ServiceImpl<CourseAttachmentDownloadLogMapper, CourseAttachmentDownloadLog>
        implements CourseAttachmentDownloadLogService {
    @Override
    public PaginationResult<CourseAttachmentDownloadLog> paginate(
            int page, int size, CourseAttachmentDownloadLogPaginateFiler filter) {
        filter.setPageStart((page - 1) * size);
        filter.setPageSize(size);

        PaginationResult<CourseAttachmentDownloadLog> pageResult = new PaginationResult<>();
        pageResult.setData(getBaseMapper().paginate(filter));
        pageResult.setTotal(getBaseMapper().paginateCount(filter));

        return pageResult;
    }
}
