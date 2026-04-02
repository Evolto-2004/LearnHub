package xyz.learnhub.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

/**
 *
 * @create 2023/2/23 16:12
 */
@Data
public class UserImportRequest {

    @Data
    public static class UserItem {
        private String deps;
        private String email;
        private String name;
        private String password;

        @JsonProperty("id_card")
        private String idCard;
    }

    @NotNull(message = "请导入数据")
    private List<UserItem> users;

    @NotNull(message = "起始行")
    @JsonProperty("start_line")
    private Integer startLine;
}
