package xyz.learnhub.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

/**
 *
 * @create 2023/3/14 11:07
 */
@Data
public class ResourceCategoryParentRequest {
    @NotNull(message = "参数为空")
    private List<Integer> ids;

    @NotNull(message = "参数为空")
    private Integer id;

    @NotNull(message = "参数为空")
    @JsonProperty("parent_id")
    private Integer parentId;
}
