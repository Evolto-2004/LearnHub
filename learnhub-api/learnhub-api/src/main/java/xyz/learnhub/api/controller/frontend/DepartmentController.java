package xyz.learnhub.api.controller.frontend;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.learnhub.common.domain.Department;
import xyz.learnhub.common.service.DepartmentService;
import xyz.learnhub.common.types.JsonResponse;

/**
 *
 * @create 2023/3/13 16:23
 */
@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    @Autowired private DepartmentService departmentService;

    @GetMapping("/index")
    public JsonResponse index() {
        return JsonResponse.data(
                departmentService.all().stream()
                        .collect(Collectors.groupingBy(Department::getParentId)));
    }
}
