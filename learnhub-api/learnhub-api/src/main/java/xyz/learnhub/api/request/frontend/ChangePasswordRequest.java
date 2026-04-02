package xyz.learnhub.api.request.frontend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 * @create 2023/3/13 09:22
 */
@Data
public class ChangePasswordRequest {

    @NotBlank(message = "请输入原密码")
    @JsonProperty("old_password")
    private String oldPassword;

    @NotBlank(message = "请输入新密码")
    @JsonProperty("new_password")
    private String newPassword;
}
