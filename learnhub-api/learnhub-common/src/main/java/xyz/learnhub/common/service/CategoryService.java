package xyz.learnhub.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;
import xyz.learnhub.common.domain.Category;
import xyz.learnhub.common.exception.NotFoundException;

/**
 * @author tengteng
 * @description 针对表【resource_categories】的数据库操作Service
 * @createDate 2023-02-23 09:50:18
 */
public interface CategoryService extends IService<Category> {

    List<Category> listByParentId(Integer id);

    List<Category> all();

    Category findOrFail(Integer id) throws NotFoundException;

    void deleteById(Integer id) throws NotFoundException;

    void update(Category category, String name, Integer parentId, Integer sort)
            throws NotFoundException;

    void create(String name, Integer parentId, Integer sort) throws NotFoundException;

    String childrenParentChain(Category category);

    String compParentChain(Integer parentId) throws NotFoundException;

    void resetSort(List<Integer> ids);

    void changeParent(Integer id, Integer parentId, List<Integer> ids) throws NotFoundException;

    Map<Integer, List<Category>> groupByParent();

    Map<Integer, String> id2name();

    Long total();

    List<Category> getChildCategorysByParentId(Integer parentId);
}
