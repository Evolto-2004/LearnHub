package xyz.learnhub.api.request.backend;

import java.util.List;
import lombok.Data;

@Data
public class CourseAttachmentSortRequest {
    private List<Integer> ids;
}
