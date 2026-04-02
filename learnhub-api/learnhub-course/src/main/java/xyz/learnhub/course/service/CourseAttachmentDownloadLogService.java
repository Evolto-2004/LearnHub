package xyz.learnhub.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import xyz.learnhub.common.types.paginate.CourseAttachmentDownloadLogPaginateFiler;
import xyz.learnhub.common.types.paginate.PaginationResult;
import xyz.learnhub.course.domain.CourseAttachmentDownloadLog;

@Service
public interface CourseAttachmentDownloadLogService extends IService<CourseAttachmentDownloadLog> {
    PaginationResult<CourseAttachmentDownloadLog> paginate(
            int page, int size, CourseAttachmentDownloadLogPaginateFiler filter);
}
