package xyz.learnhub.common.types.paginate;

import lombok.Data;

/**
 *
 * @create 2023/3/24 16:10
 */
@Data
public class UserCourseRecordPaginateFilter {
    private Integer courseId;
    private String email;
    private String name;
    private String idCard;
    private String sortField;
    private String sortAlgo;
    private Integer pageStart;
    private Integer pageSize;
    private Integer userId;
    private Integer isFinished;
}
