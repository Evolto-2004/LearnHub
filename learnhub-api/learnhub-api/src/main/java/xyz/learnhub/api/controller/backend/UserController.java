package xyz.learnhub.api.controller.backend;

import cn.hutool.core.date.DateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.learnhub.api.event.UserCourseHourRecordDestroyEvent;
import xyz.learnhub.api.event.UserCourseRecordDestroyEvent;
import xyz.learnhub.api.event.UserDestroyEvent;
import xyz.learnhub.api.request.backend.UserRequest;
import xyz.learnhub.common.annotation.BackendPermission;
import xyz.learnhub.common.annotation.Log;
import xyz.learnhub.common.constant.*;
import xyz.learnhub.common.domain.*;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.common.service.*;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.common.types.mapper.UserCourseHourRecordCourseCountMapper;
import xyz.learnhub.common.types.paginate.PaginationResult;
import xyz.learnhub.common.types.paginate.UserCourseHourRecordPaginateFilter;
import xyz.learnhub.common.types.paginate.UserCourseRecordPaginateFilter;
import xyz.learnhub.common.types.paginate.UserPaginateFilter;
import xyz.learnhub.common.util.StringUtil;
import xyz.learnhub.course.domain.*;
import xyz.learnhub.course.service.*;
import xyz.learnhub.resource.service.ResourceService;

/**
 *
 * @create 2023/2/23 09:48
 */
@RestController
@Slf4j
@RequestMapping("/backend/v1/user")
public class UserController {

    @Autowired private UserService userService;

    @Autowired private DepartmentService departmentService;

    @Autowired private ApplicationContext context;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private CourseHourService courseHourService;

    @Autowired private CourseService courseService;

    @Autowired private UserLearnDurationStatsService userLearnDurationStatsService;

    @Autowired private ApplicationContext ctx;

    @Autowired private ResourceService resourceService;

    @BackendPermission(slug = BPermissionConstant.USER_INDEX)
    @GetMapping("/index")
    @Log(title = "学员-列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");

        String name = MapUtils.getString(params, "name");
        String email = MapUtils.getString(params, "email");
        String idCard = MapUtils.getString(params, "id_card");
        Integer isActive = MapUtils.getInteger(params, "is_active");
        Integer isLock = MapUtils.getInteger(params, "is_lock");
        Integer isVerify = MapUtils.getInteger(params, "is_verify");
        Integer isSetPassword = MapUtils.getInteger(params, "is_set_password");
        String createdAt = MapUtils.getString(params, "created_at");
        String depIdsStr = MapUtils.getString(params, "dep_ids");
        List<Integer> depIds = null;
        if (StringUtil.isNotEmpty(depIdsStr)) {
            depIds = new ArrayList<>();
            if (!"0".equals(depIdsStr)) {
                List<Department> departmentList =
                        departmentService.chunk(
                                Arrays.stream(depIdsStr.split(",")).map(Integer::valueOf).toList());
                if (StringUtil.isNotEmpty(departmentList)) {
                    for (Department dep : departmentList) {
                        depIds.add(dep.getId());
                        String parentChain = "";
                        if (StringUtil.isEmpty(dep.getParentChain())) {
                            parentChain = dep.getId() + "";
                        } else {
                            parentChain = dep.getParentChain() + "," + dep.getId();
                        }
                        // 获取所有子部门ID
                        List<Department> childDepartmentList =
                                departmentService.getChildDepartmentsByParentChain(
                                        dep.getId(), parentChain);
                        if (StringUtil.isNotEmpty(childDepartmentList)) {
                            depIds.addAll(
                                    childDepartmentList.stream().map(Department::getId).toList());
                        }
                    }
                }
            }
        }

        List<Integer> finalDepIds = depIds;
        UserPaginateFilter filter =
                new UserPaginateFilter() {
                    {
                        setName(name);
                        setEmail(email);
                        setIdCard(idCard);
                        setIsActive(isActive);
                        setIsLock(isLock);
                        setIsVerify(isVerify);
                        setIsSetPassword(isSetPassword);
                        setDepIds(finalDepIds);
                        setSortAlgo(sortAlgo);
                        setSortField(sortField);
                    }
                };

        if (createdAt != null && !createdAt.trim().isEmpty()) {
            filter.setCreatedAt(createdAt.split(","));
        }

        PaginationResult<User> result = userService.paginate(page, size, filter);

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put(
                "user_dep_ids",
                userService.getDepIdsGroup(result.getData().stream().map(User::getId).toList()));
        data.put("departments", departmentService.id2name());
        data.put("pure_total", userService.total());
        data.put("dep_user_count", departmentService.getDepartmentsUserCount());

        // 课程封面资源ID
        data.put(
                "resource_url",
                resourceService.chunksPreSignUrlByIds(
                        result.getData().stream().map(User::getAvatar).toList()));

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.USER_STORE)
    @GetMapping("/create")
    @Log(title = "学员-新建", businessType = BusinessTypeConstant.GET)
    public JsonResponse create() {
        return JsonResponse.data(null);
    }

    @BackendPermission(slug = BPermissionConstant.USER_STORE)
    @PostMapping("/create")
    @Log(title = "学员-新建", businessType = BusinessTypeConstant.INSERT)
    public JsonResponse store(@RequestBody @Validated UserRequest req) {
        String email = req.getEmail();
        if (userService.emailIsExists(email)) {
            return JsonResponse.error("邮箱已存在");
        }
        String password = req.getPassword();
        if (password.isEmpty()) {
            return JsonResponse.error("请输入密码");
        }
        userService.createWithDepIds(
                email,
                req.getName(),
                req.getAvatar(),
                req.getPassword(),
                req.getIdCard(),
                req.getDepIds());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.USER_UPDATE)
    @GetMapping("/{id}")
    @Log(title = "学员-编辑", businessType = BusinessTypeConstant.GET)
    public JsonResponse edit(@PathVariable(name = "id") Integer id) throws NotFoundException {
        User user = userService.findOrFail(id);

        List<Integer> depIds = userService.getDepIdsByUserId(user.getId());

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("dep_ids", depIds);

        // 获取签名url
        data.put(
                "resource_url",
                resourceService.chunksPreSignUrlByIds(
                        new ArrayList<>() {
                            {
                                add(user.getAvatar());
                            }
                        }));

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.USER_UPDATE)
    @PutMapping("/{id}")
    @Transactional
    @Log(title = "学员-编辑", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse update(
            @PathVariable(name = "id") Integer id, @RequestBody @Validated UserRequest req)
            throws NotFoundException {
        User user = userService.findOrFail(id);

        String email = req.getEmail();
        if (!email.equals(user.getEmail()) && userService.emailIsExists(email)) {
            return JsonResponse.error("邮箱已存在");
        }

        userService.updateWithDepIds(
                user,
                email,
                req.getName(),
                req.getAvatar(),
                req.getPassword(),
                req.getIdCard(),
                req.getDepIds());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.USER_DESTROY)
    @DeleteMapping("/{id}")
    @Log(title = "学员-删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) throws NotFoundException {
        User user = userService.findOrFail(id);
        userService.removeById(user.getId());
        context.publishEvent(new UserDestroyEvent(this, user.getId()));
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.USER_LEARN)
    @GetMapping("/{id}/learn-hours")
    @SneakyThrows
    @Log(title = "学员-已学习课时列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse learnHours(
            @PathVariable(name = "id") Integer id, @RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");
        Integer isFinished = MapUtils.getInteger(params, "is_finished");

        UserCourseHourRecordPaginateFilter filter = new UserCourseHourRecordPaginateFilter();
        filter.setSortAlgo(sortAlgo);
        filter.setSortField(sortField);
        filter.setUserId(id);
        filter.setIsFinished(isFinished);

        PaginationResult<UserCourseHourRecord> result =
                userCourseHourRecordService.paginate(page, size, filter);

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put(
                "hours",
                courseHourService
                        .chunk(
                                result.getData().stream()
                                        .map(UserCourseHourRecord::getHourId)
                                        .toList())
                        .stream()
                        .collect(Collectors.toMap(CourseHour::getId, e -> e)));

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.USER_LEARN)
    @GetMapping("/{id}/learn-courses")
    @Log(title = "学员-已学习课程列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse latestLearnCourses(
            @PathVariable(name = "id") Integer id, @RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");
        Integer isFinished = MapUtils.getInteger(params, "is_finished");

        UserCourseRecordPaginateFilter filter = new UserCourseRecordPaginateFilter();
        filter.setSortAlgo(sortAlgo);
        filter.setSortField(sortField);
        filter.setUserId(id);
        filter.setIsFinished(isFinished);

        PaginationResult<UserCourseRecord> result =
                userCourseRecordService.paginate(page, size, filter);

        List<Course> courseList =
                courseService.chunks(
                        result.getData().stream().map(UserCourseRecord::getCourseId).toList());

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put("courses", courseList.stream().collect(Collectors.toMap(Course::getId, e -> e)));

        // 获取签名url
        data.put(
                "resource_url",
                resourceService.chunksPreSignUrlByIds(
                        courseList.stream().map(Course::getThumb).toList()));

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.USER_LEARN)
    @GetMapping("/{id}/all-courses")
    @Log(title = "学员-课程", businessType = BusinessTypeConstant.GET)
    public JsonResponse allCourses(@PathVariable(name = "id") Integer id) {
        // 读取学员关联的部门
        List<Integer> depIds = userService.getDepIdsByUserId(id);
        List<Department> departments = new ArrayList<>();
        HashMap<Integer, List<Course>> depCourses = new HashMap<>();
        List<Integer> courseIds = new ArrayList<>();
        List<Integer> rids = new ArrayList<>();

        if (depIds != null && !depIds.isEmpty()) {
            departments = departmentService.chunk(depIds);
            Map<Integer, Department> departmentMap = new HashMap<>();
            if (StringUtil.isNotEmpty(departments)) {
                departmentMap =
                        departments.stream().collect(Collectors.toMap(Department::getId, e -> e));
            }
            Map<Integer, Department> finalDepartmentMap = departmentMap;
            depIds.forEach(
                    (depId) -> {
                        // 查询所有的父级部门ID
                        List<Integer> allDepIds = new ArrayList<>();
                        allDepIds.add(depId);
                        Department department = finalDepartmentMap.get(depId);
                        String parentChain = department.getParentChain();
                        if (StringUtil.isNotEmpty(parentChain)) {
                            List<Integer> parentChainList =
                                    Arrays.stream(parentChain.split(","))
                                            .map(Integer::parseInt)
                                            .toList();
                            if (StringUtil.isNotEmpty(parentChainList)) {
                                allDepIds.addAll(parentChainList);
                            }
                        }
                        List<Course> tmpCourses = courseService.getDepCoursesAndShow(allDepIds);
                        depCourses.put(depId, tmpCourses);

                        if (tmpCourses != null && !tmpCourses.isEmpty()) {
                            courseIds.addAll(tmpCourses.stream().map(Course::getId).toList());
                            rids.addAll(tmpCourses.stream().map(Course::getThumb).toList());
                        }
                    });
        }

        // 未关联部门课程
        List<Course> openCourses = courseService.getOpenCoursesAndShow(1000);
        if (openCourses != null && !openCourses.isEmpty()) {
            courseIds.addAll(openCourses.stream().map(Course::getId).toList());
            rids.addAll(openCourses.stream().map(Course::getThumb).toList());
        }

        // 读取学员的线上课学习记录
        List<UserCourseRecord> userCourseRecords = new ArrayList<>();
        if (!courseIds.isEmpty()) {
            userCourseRecords = userCourseRecordService.chunk(id, courseIds);
        }

        // 获取学员线上课的课时学习数量(只要学习了就算，不一定需要已完成)
        Map<Integer, Integer> userCourseHourCount =
                userCourseHourRecordService.getUserCourseHourCount(id, courseIds, null).stream()
                        .collect(
                                Collectors.toMap(
                                        UserCourseHourRecordCourseCountMapper::getCourseId,
                                        UserCourseHourRecordCourseCountMapper::getTotal));

        // 获取学员每个课程最早的学习课时记录
        List<UserCourseHourRecord> perCourseEarliestRecords =
                userCourseHourRecordService.getUserPerCourseEarliestRecord(id);

        HashMap<String, Object> data = new HashMap<>();
        data.put("open_courses", openCourses);
        data.put("departments", departments);
        data.put("dep_courses", depCourses);
        data.put(
                "user_course_records",
                userCourseRecords.stream()
                        .collect(Collectors.toMap(UserCourseRecord::getCourseId, e -> e)));
        data.put("user_course_hour_count", userCourseHourCount);
        data.put(
                "per_course_earliest_records",
                perCourseEarliestRecords.stream()
                        .collect(Collectors.toMap(UserCourseHourRecord::getCourseId, e -> e)));
        // 获取签名url
        data.put("resource_url", resourceService.chunksPreSignUrlByIds(rids));
        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.USER_LEARN)
    @GetMapping("/{id}/learn-course/{courseId}")
    @SneakyThrows
    @Log(title = "学员-单个课程的学习记录", businessType = BusinessTypeConstant.GET)
    public JsonResponse learnCourseDetail(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "courseId") Integer courseId) {
        // 读取线上课下的所有课时
        List<CourseHour> hours = courseHourService.getHoursByCourseId(courseId);
        // 读取学员的课时学习记录
        List<UserCourseHourRecord> records = userCourseHourRecordService.getRecords(id, courseId);

        HashMap<String, Object> data = new HashMap<>();
        data.put("hours", hours);
        data.put(
                "learn_records",
                records.stream()
                        .collect(Collectors.toMap(UserCourseHourRecord::getHourId, e -> e)));

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.USER_LEARN)
    @GetMapping("/{id}/learn-stats")
    @SneakyThrows
    @Log(title = "学员-学习统计", businessType = BusinessTypeConstant.GET)
    public JsonResponse learn(@PathVariable(name = "id") Integer id) {
        // 最近一个月的每天学习时长
        String todayStr = DateTime.now().toDateStr();
        String startDateStr = DateTime.of(DateTime.now().getTime() - 86400000L * 30).toDateStr();
        long startTime = new DateTime(startDateStr).getTime();
        long endTime = new DateTime(todayStr).getTime();

        List<UserLearnDurationStats> monthRecords =
                userLearnDurationStatsService.dateBetween(id, startDateStr, todayStr);
        Map<String, Long> date2duration =
                monthRecords.stream()
                        .collect(
                                Collectors.toMap(
                                        e -> DateTime.of(e.getCreatedDate()).toDateStr(),
                                        UserLearnDurationStats::getDuration));

        @Data
        class StatsItem {
            private String key;
            private Long value;
        }

        List<StatsItem> data = new ArrayList<>();

        while (startTime <= endTime) {
            String dateKey = DateTime.of(startTime).toDateStr();

            Long duration = 0L;
            if (date2duration.get(dateKey) != null) {
                duration = date2duration.get(dateKey);
            }

            StatsItem tmpItem = new StatsItem();
            tmpItem.setKey(dateKey);
            tmpItem.setValue(duration);

            data.add(tmpItem);

            startTime += 86400000;
        }

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.USER_LEARN_DESTROY)
    @DeleteMapping("/{id}/learn-course/{courseId}")
    @SneakyThrows
    @Log(title = "学员-线上课学习记录删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroyUserCourse(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "courseId") Integer courseId) {
        userCourseRecordService.destroy(id, courseId);
        ctx.publishEvent(new UserCourseRecordDestroyEvent(this, id, courseId));
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.USER_LEARN_DESTROY)
    @DeleteMapping("/{id}/learn-course/{courseId}/hour/{hourId}")
    @SneakyThrows
    @Log(title = "学员-线上课课时学习记录删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroyUserHour(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "hourId") Integer hourId) {
        userCourseHourRecordService.remove(id, courseId, hourId);
        ctx.publishEvent(new UserCourseHourRecordDestroyEvent(this, id, courseId, hourId));
        return JsonResponse.success();
    }
}
