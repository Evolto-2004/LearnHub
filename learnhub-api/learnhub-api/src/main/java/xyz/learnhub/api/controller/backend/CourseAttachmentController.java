package xyz.learnhub.api.controller.backend;

import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.learnhub.api.request.backend.CourseAttachmentMultiRequest;
import xyz.learnhub.api.request.backend.CourseAttachmentRequest;
import xyz.learnhub.api.request.backend.CourseAttachmentSortRequest;
import xyz.learnhub.common.annotation.BackendPermission;
import xyz.learnhub.common.annotation.Log;
import xyz.learnhub.common.constant.BPermissionConstant;
import xyz.learnhub.common.constant.BackendConstant;
import xyz.learnhub.common.constant.BusinessTypeConstant;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.course.domain.CourseAttachment;
import xyz.learnhub.course.service.CourseAttachmentService;

@RestController
@Slf4j
@RequestMapping("/backend/v1/course/{courseId}/attachment")
public class CourseAttachmentController {

    @Autowired private CourseAttachmentService attachmentService;

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @PostMapping("/create")
    @Log(title = "线上课-附件-新建", businessType = BusinessTypeConstant.INSERT)
    public JsonResponse store(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseAttachmentRequest req)
            throws NotFoundException {
        // 附件类型校验
        String type = req.getType();
        if (!BackendConstant.RESOURCE_TYPE_ATTACHMENT.contains(type)) {
            return JsonResponse.error("附件类型不支持");
        }

        // 课时重复添加校验
        List<Integer> existsRids = attachmentService.getRidsByCourseId(courseId);
        if (existsRids != null) {
            if (existsRids.contains(req.getRid())) {
                return JsonResponse.error("附件已存在");
            }
        }

        attachmentService.create(courseId, req.getSort(), req.getTitle(), type, req.getRid());

        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @PostMapping("/create-batch")
    @Transactional
    @Log(title = "线上课-附件-批量新建", businessType = BusinessTypeConstant.INSERT)
    public JsonResponse storeMulti(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseAttachmentMultiRequest req) {
        if (req.getAttachments().isEmpty()) {
            return JsonResponse.error("参数为空");
        }

        List<Integer> existsRids = attachmentService.getRidsByCourseId(courseId);

        List<CourseAttachment> attachments = new ArrayList<>();
        Date now = new Date();

        for (CourseAttachmentMultiRequest.AttachmentItem item : req.getAttachments()) {
            if (existsRids.contains(item.getRid())) {
                return JsonResponse.error("附件《" + item.getTitle() + "》已存在");
            }

            attachments.add(
                    new CourseAttachment() {
                        {
                            setCourseId(courseId);
                            setSort(item.getSort());
                            setType(item.getType());
                            setRid(item.getRid());
                            setTitle(item.getTitle());
                            setCreatedAt(now);
                        }
                    });
        }

        attachmentService.saveBatch(attachments);
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @GetMapping("/{id}")
    @Log(title = "线上课-附件-编辑", businessType = BusinessTypeConstant.GET)
    public JsonResponse edit(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id)
            throws NotFoundException {
        CourseAttachment courseAttachment = attachmentService.findOrFail(id, courseId);
        return JsonResponse.data(courseAttachment);
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @PutMapping("/{id}")
    @Log(title = "线上课-附件-编辑", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse update(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id,
            @RequestBody @Validated CourseAttachmentRequest req)
            throws NotFoundException {
        CourseAttachment courseAttachment = attachmentService.findOrFail(id, courseId);
        attachmentService.update(courseAttachment, req.getSort(), req.getTitle());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @DeleteMapping("/{id}")
    @Log(title = "线上课-附件-删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroy(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id)
            throws NotFoundException {
        CourseAttachment courseAttachment = attachmentService.findOrFail(id, courseId);
        attachmentService.removeById(courseAttachment.getId());
        return JsonResponse.success();
    }

    @PutMapping("/update/sort")
    @Log(title = "线上课-附件-排序调整", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse updateSort(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseAttachmentSortRequest req) {
        attachmentService.updateSort(req.getIds(), courseId);
        return JsonResponse.success();
    }
}
