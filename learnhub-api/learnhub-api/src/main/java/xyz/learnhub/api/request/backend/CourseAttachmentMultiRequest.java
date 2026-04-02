package xyz.learnhub.api.request.backend;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class CourseAttachmentMultiRequest {
    @Data
    public static class AttachmentItem {
        private String title;
        private Integer sort;
        private String type;
        private Integer rid;
    }

    @NotNull(message = "attachments参数不存在")
    private List<AttachmentItem> attachments;
}
