package xyz.learnhub.api.request.backend;

import java.util.List;
import lombok.Data;

/**
 *
 * @create 2023/3/20 10:45
 */
@Data
public class CourseHourSortRequest {
    private List<Integer> ids;
}
