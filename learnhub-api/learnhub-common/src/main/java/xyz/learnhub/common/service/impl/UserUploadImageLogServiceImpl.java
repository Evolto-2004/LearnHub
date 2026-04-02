package xyz.learnhub.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.learnhub.common.domain.UserUploadImageLog;
import xyz.learnhub.common.mapper.UserUploadImageLogMapper;
import xyz.learnhub.common.service.UserUploadImageLogService;

/**
 * @author tengteng
 * @description 针对表【user_upload_image_logs】的数据库操作Service实现
 * @createDate 2023-03-24 14:32:48
 */
@Service
public class UserUploadImageLogServiceImpl
        extends ServiceImpl<UserUploadImageLogMapper, UserUploadImageLog>
        implements UserUploadImageLogService {}
