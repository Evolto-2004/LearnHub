package xyz.learnhub.api.request.backend;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
public class LoginRequest implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    @NotNull(message = "请输入邮箱")
    public String email;

    @NotNull(message = "请输入密码")
    public String password;
}
