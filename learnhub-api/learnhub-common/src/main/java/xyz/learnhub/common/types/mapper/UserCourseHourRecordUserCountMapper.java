package xyz.learnhub.common.types.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @create 2023/5/8 11:22
 */
@Data
public class UserCourseHourRecordUserCountMapper {
    @JsonProperty("user_id")
    private Integer userId;

    private Integer total;
}
