package xyz.learnhub.api.request.backend;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

/**
 *
 * @create 2023/3/24 16:22
 */
@Data
public class CourseUserDestroyRequest {
    @NotNull(message = "ids参数不存在")
    private List<Integer> ids;
}
