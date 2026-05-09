package xyz.learnhub.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.learnhub.common.domain.AdminLog;
import xyz.learnhub.common.mapper.AdminLogMapper;
import xyz.learnhub.common.service.AdminLogService;

/**
 * @author tengteng
 * @description 针对表【admin_logs】的数据库操作Service实现
 * @createDate 2023-02-17 15:40:31
 */
@Service
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog>
        implements AdminLogService {}
