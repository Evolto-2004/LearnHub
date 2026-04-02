package xyz.learnhub.api.request.backend;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

/**
 *
 * @create 2023/3/20 10:42
 */
@Data
public class CourseChapterSortRequest {
    @NotNull(message = "ids参数不存在")
    private List<Integer> ids;
}
