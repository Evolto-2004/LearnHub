package xyz.learnhub.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 * @create 2023/3/2 15:41
 */
@Data
public class PasswordChangeRequest {

    @JsonProperty("old_password")
    @NotBlank(message = "请输入原密码")
    private String oldPassword;

    @JsonProperty("new_password")
    @NotBlank(message = "请输入新密码")
    private String newPassword;
}
