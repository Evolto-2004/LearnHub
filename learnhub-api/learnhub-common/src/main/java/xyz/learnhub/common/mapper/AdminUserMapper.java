package xyz.learnhub.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.learnhub.common.domain.AdminUser;

@Mapper
/**
 * @author tengteng
 * @description 针对表【admin_users】的数据库操作Mapper
 * @createDate 2023-02-11 10:58:52
 */
public interface AdminUserMapper extends BaseMapper<AdminUser> {}
