package xyz.learnhub.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ResourceUpdateRequest {

    @NotBlank(message = "请输入资源名")
    @Length(min = 1, max = 254, message = "资源名长度在1-254个字符之间")
    private String name;

    @NotNull(message = "category_id参数不存在")
    @JsonProperty("category_id")
    private Integer categoryId;
}
