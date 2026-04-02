package xyz.learnhub.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.domain.Department;
import xyz.learnhub.common.types.mapper.DepartmentsUserCountMapRes;

/**
 * @author tengteng
 * @description 针对表【departments】的数据库操作Mapper
 * @createDate 2023-02-19 12:19:45
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    List<DepartmentsUserCountMapRes> getDepartmentsUserCount();
}
