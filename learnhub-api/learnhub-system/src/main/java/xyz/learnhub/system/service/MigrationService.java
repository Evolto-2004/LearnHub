package xyz.learnhub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import xyz.learnhub.system.domain.Migration;

/**
 * @author tengyongzhi
 * @description 针对表【migrations】的数据库操作Service
 * @createDate 2023-08-27 12:40:00
 */
public interface MigrationService extends IService<Migration> {
    List<String> all();

    void store(String name);
}
