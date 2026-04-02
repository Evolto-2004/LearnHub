package xyz.learnhub.common.types.paginate;

import lombok.Data;

@Data
public class AdminLogPaginateFiler {

    private Integer adminId;

    private String adminName;

    private String module;

    private String title;

    private Integer opt;

    private String startTime;

    private String endTime;

    private String sortField;

    private String sortAlgo;

    private Integer pageStart;

    private Integer pageSize;
}
