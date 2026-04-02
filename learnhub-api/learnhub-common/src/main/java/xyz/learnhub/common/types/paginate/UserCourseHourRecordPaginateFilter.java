package xyz.learnhub.common.types.paginate;

import lombok.Data;

/**
 *
 * @create 2023/4/17 17:10
 */
@Data
public class UserCourseHourRecordPaginateFilter {
    private Integer userId;
    private Integer pageStart;
    private Integer pageSize;
    private String sortField;
    private String sortAlgo;
    private Integer isFinished;
}
