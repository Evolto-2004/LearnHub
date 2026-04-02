package xyz.learnhub.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;
import xyz.learnhub.common.domain.Department;
import xyz.learnhub.common.exception.NotFoundException;

/**
 * @author tengteng
 * @description 针对表【departments】的数据库操作Service
 * @createDate 2023-02-19 10:39:57
 */
public interface DepartmentService extends IService<Department> {

    List<Department> listByParentId(Integer id);

    List<Department> all();

    Department findOrFail(Integer id) throws NotFoundException;

    void destroy(Integer id) throws NotFoundException;

    void update(Department department, String name, Integer parentId, Integer sort)
            throws NotFoundException;

    String compParentChain(Integer parentId) throws NotFoundException;

    String childrenParentChain(Department department);

    Integer create(String name, Integer parentId, Integer sort) throws NotFoundException;

    void remoteRelateUsersByDepId(Integer depId);

    List<Integer> getUserIdsByDepId(Integer depId);

    void changeParent(Integer id, Integer parentId, List<Integer> ids) throws NotFoundException;

    void resetSort(List<Integer> ids);

    Map<Integer, List<Department>> groupByParent();

    Map<Integer, String> id2name();

    Long total();

    Map<Integer, Integer> getDepartmentsUserCount();

    List<Department> chunk(List<Integer> ids);

    Integer createWithChainList(List<String> ou);

    Department findByName(String name, Integer parentId);

    List<Department> getChildDepartmentsByParentId(Integer parentId);

    List<Department> getChildDepartmentsByParentChain(Integer parentId, String parentChain);
}
