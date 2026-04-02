package xyz.learnhub.api.event;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @create 2023/3/21 14:31
 */
@Setter
@Getter
public class UserLogoutEvent extends ApplicationEvent {
    private Integer userId;
    private String jti;
    private Date createdAt;

    public UserLogoutEvent(Object source, Integer userId, String jti) {
        super(source);
        this.userId = userId;
        this.jti = jti;
        this.createdAt = new Date();
    }
}
