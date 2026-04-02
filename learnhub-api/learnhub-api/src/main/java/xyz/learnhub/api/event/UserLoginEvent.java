package xyz.learnhub.api.event;

import cn.hutool.http.useragent.UserAgent;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @create 2023/3/10 13:22
 */
@Setter
@Getter
public class UserLoginEvent extends ApplicationEvent {

    private Integer userId;

    private String email;

    private Date loginAt;

    private String token;

    private String ip;

    private UserAgent userAgent;

    public UserLoginEvent(
            Object source,
            Integer userId,
            String email,
            String token,
            String ip,
            UserAgent userAgent) {
        super(source);
        this.userId = userId;
        this.email = email;
        this.token = token;
        this.ip = ip;
        this.userAgent = userAgent;
        this.loginAt = new Date();
    }
}
