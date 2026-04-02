package xyz.learnhub.common.types.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Data;

/**
 *
 * @create 2023/5/8 14:42
 */
@Data
public class UserCourseHourRecordUserFirstCreatedAtMapper {
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("created_at")
    private Date createdAt;
}
