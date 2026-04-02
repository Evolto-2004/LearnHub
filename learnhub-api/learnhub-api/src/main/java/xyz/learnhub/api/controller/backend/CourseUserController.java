package xyz.learnhub.api.controller.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.learnhub.api.event.UserCourseRecordDestroyEvent;
import xyz.learnhub.api.request.backend.CourseUserDestroyRequest;
import xyz.learnhub.common.annotation.BackendPermission;
import xyz.learnhub.common.annotation.Log;
import xyz.learnhub.common.constant.BPermissionConstant;
import xyz.learnhub.common.constant.BusinessTypeConstant;
import xyz.learnhub.common.domain.User;
import xyz.learnhub.common.service.*;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.common.types.mapper.UserCourseHourRecordUserFirstCreatedAtMapper;
import xyz.learnhub.common.types.paginate.PaginationResult;
import xyz.learnhub.common.types.paginate.UserPaginateFilter;
import xyz.learnhub.course.domain.UserCourseHourRecord;
import xyz.learnhub.course.domain.UserCourseRecord;
import xyz.learnhub.course.service.CourseService;
import xyz.learnhub.course.service.UserCourseHourRecordService;
import xyz.learnhub.course.service.UserCourseRecordService;
import xyz.learnhub.resource.service.ResourceService;

/**
 *
 * @create 2023/3/24 16:08
 */
@RestController
@Slf4j
@RequestMapping("/backend/v1/course/{courseId}/user")
public class CourseUserController {

    @Autowired private CourseService courseService;

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private UserService userService;

    @Autowired private DepartmentService departmentService;

    @Autowired private ApplicationContext ctx;

    @Autowired private ResourceService resourceService;

    @BackendPermission(slug = BPermissionConstant.COURSE_USER)
    @GetMapping("/index")
    @SneakyThrows
    @Log(title = "线上课-学习记录-列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse index(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");
        String name = MapUtils.getString(params, "name");
        String email = MapUtils.getString(params, "email");
        String idCard = MapUtils.getString(params, "id_card");
        Integer depId = MapUtils.getInteger(params, "dep_id");

        UserPaginateFilter filter = new UserPaginateFilter();
        filter.setName(name);
        filter.setEmail(email);
        filter.setSortAlgo(sortAlgo);
        filter.setSortField(sortField);
        filter.setIdCard(idCard);

        // 所属部门
        if (depId != null && depId > 0) { // 设置过滤部门
            filter.setDepIds(
                    new ArrayList<>() {
                        {
                            add(depId);
                        }
                    });
        } else { // 默认读取课程关联的全部部门
            List<Integer> depIds = courseService.getDepIdsByCourseId(courseId);
            if (depIds != null && !depIds.isEmpty()) {
                filter.setDepIds(depIds);
            }
        }

        PaginationResult<User> result = userService.paginate(page, size, filter);

        List<Integer> userIds = result.getData().stream().map(User::getId).toList();

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put(
                "user_course_records",
                userCourseRecordService
                        .chunk(
                                userIds,
                                new ArrayList<>() {
                                    {
                                        add(courseId);
                                    }
                                })
                        .stream()
                        .collect(Collectors.toMap(UserCourseRecord::getUserId, e -> e)));
        data.put(
                "user_course_hour_user_first_at",
                userCourseHourRecordService
                        .getUserCourseHourUserFirstCreatedAt(courseId, userIds)
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        UserCourseHourRecordUserFirstCreatedAtMapper::getUserId,
                                        UserCourseHourRecordUserFirstCreatedAtMapper
                                                ::getCreatedAt)));
        data.put("course", courseService.findOrFail(courseId));
        data.put(
                "user_dep_ids",
                userService.getDepIdsGroup(result.getData().stream().map(User::getId).toList()));
        data.put("departments", departmentService.id2name());

        // 获取每个学员的最早学习时间
        List<UserCourseHourRecord> perUserEarliestRecords =
                userCourseHourRecordService.getCoursePerUserEarliestRecord(courseId);
        data.put(
                "per_user_earliest_records",
                perUserEarliestRecords.stream()
                        .collect(Collectors.toMap(UserCourseHourRecord::getUserId, e -> e)));

        // 获取签名url
        data.put(
                "resource_url",
                resourceService.chunksPreSignUrlByIds(
                        result.getData().stream().map(User::getAvatar).toList()));
        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.COURSE_USER_DESTROY)
    @PostMapping("/destroy")
    @Log(title = "线上课-学习记录-删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroy(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseUserDestroyRequest req) {
        if (req.getIds().isEmpty()) {
            return JsonResponse.error("请选择需要删除的数据");
        }
        List<UserCourseRecord> records =
                userCourseRecordService.chunks(
                        req.getIds(),
                        new ArrayList<>() {
                            {
                                add("user_id");
                                add("id");
                            }
                        });
        for (UserCourseRecord record : records) {
            userCourseRecordService.removeById(record);
            ctx.publishEvent(new UserCourseRecordDestroyEvent(this, record.getUserId(), courseId));
        }
        return JsonResponse.success();
    }
}
