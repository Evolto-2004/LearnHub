package xyz.learnhub.api.controller.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.learnhub.common.domain.Category;
import xyz.learnhub.common.service.CategoryService;
import xyz.learnhub.common.types.JsonResponse;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired private CategoryService categoryService;

    @GetMapping("/all")
    public JsonResponse all() {
        List<Category> categories = categoryService.all();
        HashMap<String, Object> data = new HashMap<>();
        data.put(
                "categories",
                categories.stream().collect(Collectors.groupingBy(Category::getParentId)));
        return JsonResponse.data(data);
    }
}
