package xyz.learnhub.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.learnhub.api.event.CourseChapterDestroyEvent;
import xyz.learnhub.api.request.backend.CourseChapterRequest;
import xyz.learnhub.api.request.backend.CourseChapterSortRequest;
import xyz.learnhub.common.annotation.BackendPermission;
import xyz.learnhub.common.annotation.Log;
import xyz.learnhub.common.constant.BPermissionConstant;
import xyz.learnhub.common.constant.BusinessTypeConstant;
import xyz.learnhub.common.context.BCtx;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.course.domain.CourseChapter;
import xyz.learnhub.course.service.CourseChapterService;
import xyz.learnhub.course.service.CourseHourService;

@RestController
@RequestMapping("/backend/v1/course/{courseId}/chapter")
public class CourseChapterController {

    @Autowired private CourseChapterService chapterService;

    @Autowired private CourseHourService hourService;

    @Autowired private ApplicationContext ctx;

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @PostMapping("/create")
    @Log(title = "线上课-章节-新建", businessType = BusinessTypeConstant.GET)
    public JsonResponse store(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseChapterRequest req) {
        chapterService.create(courseId, req.getName(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @GetMapping("/{id}")
    @Log(title = "线上课-章节-编辑", businessType = BusinessTypeConstant.GET)
    public JsonResponse edit(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id)
            throws NotFoundException {
        CourseChapter chapter = chapterService.findOrFail(id, courseId);
        return JsonResponse.data(chapter);
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @PutMapping("/{id}")
    @Log(title = "线上课-章节-编辑", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse update(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id,
            @RequestBody @Validated CourseChapterRequest req)
            throws NotFoundException {
        CourseChapter chapter = chapterService.findOrFail(id, courseId);
        chapterService.update(chapter, req.getName(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @DeleteMapping("/{id}")
    @Log(title = "线上课-章节-删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroy(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id)
            throws NotFoundException {
        CourseChapter chapter = chapterService.findOrFail(id, courseId);
        if (hourService.getCountByChapterId(chapter.getId()) > 0) {
            return JsonResponse.error("当前章节下面存在课时无法删除");
        }
        chapterService.removeById(chapter.getId());
        ctx.publishEvent(
                new CourseChapterDestroyEvent(
                        this, BCtx.getId(), chapter.getCourseId(), chapter.getId()));
        return JsonResponse.success();
    }

    @PutMapping("/update/sort")
    @Log(title = "线上课-章节-更新排序", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse updateSort(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseChapterSortRequest req) {
        chapterService.updateSort(req.getIds(), courseId);
        return JsonResponse.success();
    }
}
