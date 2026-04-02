package xyz.learnhub.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.domain.User;
import xyz.learnhub.common.types.paginate.UserPaginateFilter;

/**
 * @author tengteng
 * @description 针对表【users】的数据库操作Mapper
 * @createDate 2023-03-20 13:37:33
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> paginate(UserPaginateFilter filter);

    Long paginateCount(UserPaginateFilter filter);
}
