package xyz.learnhub.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.types.paginate.CourseAttachmentDownloadLogPaginateFiler;
import xyz.learnhub.course.domain.CourseAttachmentDownloadLog;

/**
 * @author tengteng
 * @description 针对表【course_attachment_download_log】的数据库操作Mapper
 * @createDate 2023-08-02 17:34:01
 */
@Mapper
public interface CourseAttachmentDownloadLogMapper extends BaseMapper<CourseAttachmentDownloadLog> {

    List<CourseAttachmentDownloadLog> paginate(CourseAttachmentDownloadLogPaginateFiler filer);

    Long paginateCount(CourseAttachmentDownloadLogPaginateFiler filer);
}
