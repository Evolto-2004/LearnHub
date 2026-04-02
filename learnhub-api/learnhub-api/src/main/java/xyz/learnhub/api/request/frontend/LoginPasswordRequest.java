package xyz.learnhub.api.request.frontend;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginPasswordRequest {

    @NotBlank(message = "请输入邮箱")
    private String email;

    @NotBlank(message = "请输入密码")
    private String password;
}
