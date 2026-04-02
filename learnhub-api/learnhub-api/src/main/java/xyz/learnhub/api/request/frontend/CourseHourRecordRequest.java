package xyz.learnhub.api.request.frontend;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @create 2023/3/20 17:12
 */
@Data
public class CourseHourRecordRequest {
    @NotNull(message = "duration参数不存在")
    private Integer duration;
}
