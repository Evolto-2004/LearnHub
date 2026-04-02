package xyz.learnhub.common.types.paginate;

import java.util.List;
import lombok.Data;

/**
 *
 * @create 2023/2/23 11:18
 */
@Data
public class ResourcePaginateFilter {

    private String name;

    private String extension;

    private String disk;

    private String sortField;

    private String sortAlgo;

    private List<Integer> categoryIds;

    private String type;

    private Integer adminId;

    private Integer pageStart;

    private Integer pageSize;
}
