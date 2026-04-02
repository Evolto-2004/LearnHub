package xyz.learnhub.common.types.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @create 2023/3/29 10:01
 */
@Data
public class UserCourseHourRecordCourseCountMapper {
    @JsonProperty("course_id")
    private Integer courseId;

    private Integer total;
}
