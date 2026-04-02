package xyz.learnhub.api.request.backend;

import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import lombok.Data;

/**
 *
 * @create 2023/3/9 16:13
 */
@Data
public class AppConfigRequest {

    @NotNull(message = "配置为空")
    private HashMap<String, String> data;
}
