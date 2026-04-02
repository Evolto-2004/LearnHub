package xyz.learnhub.api.event;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @create 2023/2/23 13:51
 */
@Getter
@Setter
public class UserDestroyEvent extends ApplicationEvent {

    private Integer userId;
    private Date createdAt;

    public UserDestroyEvent(Object source, Integer userId) {
        super(source);
        this.userId = userId;
        this.createdAt = new Date();
    }
}
