package xyz.learnhub.api.request.backend;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

/**
 *
 * @create 2023/3/14 11:02
 */
@Data
public class ResourceCategorySortRequest {

    @NotNull(message = "参数为空")
    private List<Integer> ids;
}
