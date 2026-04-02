package xyz.learnhub.api.request.backend;

import java.util.List;
import lombok.Data;

@Data
public class ResourceDestroyMultiRequest {
    private List<Integer> ids;
}
