package xyz.learnhub.common.types.paginate;

import lombok.Data;

@Data
public class CourseAttachmentDownloadLogPaginateFiler {

    private Integer userId;

    private Integer courseId;

    private String title;

    private Integer courserAttachmentId;

    private Integer rid;

    private String sortField;

    private String sortAlgo;

    private Integer pageStart;

    private Integer pageSize;
}
