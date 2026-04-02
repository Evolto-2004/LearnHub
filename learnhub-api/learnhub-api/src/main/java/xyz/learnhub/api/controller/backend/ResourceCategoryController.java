package xyz.learnhub.api.controller.backend;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.learnhub.api.event.ResourceCategoryDestroyEvent;
import xyz.learnhub.api.request.backend.ResourceCategoryParentRequest;
import xyz.learnhub.api.request.backend.ResourceCategoryRequest;
import xyz.learnhub.api.request.backend.ResourceCategorySortRequest;
import xyz.learnhub.common.annotation.BackendPermission;
import xyz.learnhub.common.annotation.Log;
import xyz.learnhub.common.constant.BPermissionConstant;
import xyz.learnhub.common.constant.BackendConstant;
import xyz.learnhub.common.constant.BusinessTypeConstant;
import xyz.learnhub.common.context.BCtx;
import xyz.learnhub.common.domain.Category;
import xyz.learnhub.common.exception.NotFoundException;
import xyz.learnhub.common.service.CategoryService;
import xyz.learnhub.common.types.JsonResponse;
import xyz.learnhub.course.service.CourseCategoryService;
import xyz.learnhub.course.service.CourseService;
import xyz.learnhub.resource.domain.Resource;
import xyz.learnhub.resource.service.ResourceCategoryService;
import xyz.learnhub.resource.service.ResourceService;

/**
 *
 * @create 2023/2/23 09:46
 */
@RestController
@RequestMapping("/backend/v1/resource-category")
public class ResourceCategoryController {

    @Autowired private CategoryService categoryService;

    @Autowired private CourseService courseService;

    @Autowired private ResourceService resourceService;

    @Autowired private ResourceCategoryService resourceCategoryService;

    @Autowired private CourseCategoryService courseCategoryService;

    @Autowired private ApplicationContext ctx;

    @GetMapping("/index")
    @Log(title = "资源-分类-列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse index() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categoryService.groupByParent());
        return JsonResponse.data(data);
    }

    @GetMapping("/categories")
    @Log(title = "资源-分类-全部分类", businessType = BusinessTypeConstant.GET)
    public JsonResponse index(
            @RequestParam(name = "parent_id", defaultValue = "0") Integer parentId) {
        List<Category> categories = categoryService.listByParentId(parentId);
        return JsonResponse.data(categories);
    }

    @GetMapping("/create")
    @Log(title = "资源-分类-新建", businessType = BusinessTypeConstant.GET)
    public JsonResponse create() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categoryService.groupByParent());
        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PostMapping("/create")
    @Log(title = "资源-分类-新建", businessType = BusinessTypeConstant.INSERT)
    public JsonResponse store(@RequestBody @Validated ResourceCategoryRequest req)
            throws NotFoundException {
        categoryService.create(req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @GetMapping("/{id}")
    @Log(title = "资源-分类-编辑", businessType = BusinessTypeConstant.GET)
    public JsonResponse edit(@PathVariable Integer id) throws NotFoundException {
        Category category = categoryService.findOrFail(id);
        return JsonResponse.data(category);
    }

    @BackendPermission(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PutMapping("/{id}")
    @Log(title = "资源-分类-编辑", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse update(@PathVariable Integer id, @RequestBody ResourceCategoryRequest req)
            throws NotFoundException {
        Category category = categoryService.findOrFail(id);
        categoryService.update(category, req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @GetMapping("/{id}/destroy")
    @Log(title = "资源-分类-批量删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse preDestroy(@PathVariable Integer id) {
        List<Integer> courseIds = courseCategoryService.getCourseIdsByCategoryId(id);
        List<Integer> rids = resourceCategoryService.getRidsByCategoryId(id);

        HashMap<String, Object> data = new HashMap<>();
        data.put("children", categoryService.listByParentId(id));
        data.put("courses", new ArrayList<>());
        data.put("videos", new ArrayList<>());
        data.put("images", new ArrayList<>());

        if (courseIds != null && !courseIds.isEmpty()) {
            data.put(
                    "courses",
                    courseService.chunks(
                            courseIds,
                            new ArrayList<>() {
                                {
                                    add("id");
                                    add("title");
                                }
                            }));
        }

        if (rids != null && !rids.isEmpty()) {
            Map<String, List<Resource>> resources =
                    resourceService
                            .chunks(
                                    rids,
                                    new ArrayList<>() {
                                        {
                                            add("id");
                                            add("admin_id");
                                            add("type");
                                            add("name");
                                            add("url");
                                        }
                                    })
                            .stream()
                            .collect(Collectors.groupingBy(Resource::getType));
            data.put("videos", resources.get(BackendConstant.RESOURCE_TYPE_VIDEO));
            data.put("images", resources.get(BackendConstant.RESOURCE_TYPE_IMAGE));
        }

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @DeleteMapping("/{id}")
    @Log(title = "资源-分类-删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroy(@PathVariable Integer id) throws NotFoundException {
        Category category = categoryService.findOrFail(id);
        categoryService.deleteById(category.getId());
        ctx.publishEvent(new ResourceCategoryDestroyEvent(this, BCtx.getId(), category.getId()));
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PutMapping("/update/sort")
    @Log(title = "资源-分类-更新排序", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse resort(@RequestBody @Validated ResourceCategorySortRequest req) {
        categoryService.resetSort(req.getIds());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PutMapping("/update/parent")
    @Log(title = "资源-分类-更新父级", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse updateParent(@RequestBody @Validated ResourceCategoryParentRequest req)
            throws NotFoundException {
        categoryService.changeParent(req.getId(), req.getParentId(), req.getIds());
        return JsonResponse.success();
    }
}
