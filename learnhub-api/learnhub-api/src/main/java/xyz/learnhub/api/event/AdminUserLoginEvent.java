package xyz.learnhub.api.event;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class AdminUserLoginEvent extends ApplicationEvent {

    private Integer adminId;

    private Date loginAt;

    private String token;

    private String ip;

    private Integer loginTimes;

    public AdminUserLoginEvent(
            Object source, Integer adminId, String token, String ip, Integer loginTimes) {
        super(source);
        this.adminId = adminId;
        this.loginAt = new Date();
        this.token = token;
        this.ip = ip;
        this.loginTimes = loginTimes;
    }
}
