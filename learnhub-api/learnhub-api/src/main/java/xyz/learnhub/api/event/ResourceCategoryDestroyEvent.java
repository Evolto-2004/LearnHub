package xyz.learnhub.api.event;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @create 2023/2/26 17:10
 */
@Getter
@Setter
public class ResourceCategoryDestroyEvent extends ApplicationEvent {

    private Integer adminId;
    private Integer categoryId;
    private Date createdAt;

    public ResourceCategoryDestroyEvent(Object source, Integer adminId, Integer categoryId) {
        super(source);
        this.adminId = adminId;
        this.categoryId = categoryId;
        this.createdAt = new Date();
    }
}
