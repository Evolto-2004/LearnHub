package xyz.learnhub.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class ResourceCategoryChangeRequest {
    @NotNull(message = "参数为空")
    private List<Integer> ids;

    @NotNull(message = "请选择分类")
    @JsonProperty("category_id")
    private Integer categoryId;
}
