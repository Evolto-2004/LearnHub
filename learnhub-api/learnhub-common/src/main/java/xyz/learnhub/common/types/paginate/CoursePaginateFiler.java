package xyz.learnhub.common.types.paginate;

import java.util.List;
import lombok.Data;

/**
 *
 * @create 2023/2/24 15:53
 */
@Data
public class CoursePaginateFiler {

    private String title;

    private List<Integer> depIds;

    private List<Integer> categoryIds;

    private Integer isRequired;

    private String sortField;

    private String sortAlgo;

    private Integer isShow;

    private Integer pageStart;

    private Integer pageSize;

    private Integer adminId;
}
