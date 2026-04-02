package xyz.learnhub.api.controller.backend;

import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.learnhub.common.annotation.Log;
import xyz.learnhub.common.constant.BusinessTypeConstant;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.common.types.paginate.CourseAttachmentDownloadLogPaginateFiler;
import xyz.learnhub.common.types.paginate.PaginationResult;
import xyz.learnhub.course.domain.CourseAttachmentDownloadLog;
import xyz.learnhub.course.service.CourseAttachmentDownloadLogService;

@RestController
@Slf4j
@RequestMapping("/backend/v1/course/attachment/download/log")
public class CourseAttachmentDownloadLogController {

    @Autowired private CourseAttachmentDownloadLogService courseAttachmentDownloadLogService;

    @GetMapping("/index")
    @Log(title = "学员下载课件记录-列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");

        Integer userId = MapUtils.getInteger(params, "user_id");
        Integer courseId = MapUtils.getInteger(params, "course_id");
        String title = MapUtils.getString(params, "title");
        Integer courserAttachmentId = MapUtils.getInteger(params, "courser_attachment_id");
        Integer rid = MapUtils.getInteger(params, "rid");

        CourseAttachmentDownloadLogPaginateFiler filter =
                new CourseAttachmentDownloadLogPaginateFiler();
        filter.setUserId(userId);
        filter.setCourseId(courseId);
        filter.setTitle(title);
        filter.setCourserAttachmentId(courserAttachmentId);
        filter.setRid(rid);
        filter.setSortField(sortField);
        filter.setSortAlgo(sortAlgo);

        PaginationResult<CourseAttachmentDownloadLog> result =
                courseAttachmentDownloadLogService.paginate(page, size, filter);

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());

        return JsonResponse.data(data);
    }
}
