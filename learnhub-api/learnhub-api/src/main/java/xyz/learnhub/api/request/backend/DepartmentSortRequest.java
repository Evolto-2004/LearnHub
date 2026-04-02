package xyz.learnhub.api.request.backend;

import java.util.List;
import lombok.Data;

/**
 *
 * @create 2023/3/14 11:11
 */
@Data
public class DepartmentSortRequest {
    private List<Integer> ids;
}
