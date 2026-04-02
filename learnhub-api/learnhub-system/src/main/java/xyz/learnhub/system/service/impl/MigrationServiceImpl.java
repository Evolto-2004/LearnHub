package xyz.learnhub.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import xyz.learnhub.system.domain.Migration;
import xyz.learnhub.system.mapper.MigrationMapper;
import xyz.learnhub.system.service.MigrationService;

/**
 * @author tengyongzhi
 * @description 针对表【migrations】的数据库操作Service实现
 * @createDate 2023-08-27 12:40:00
 */
@Service
public class MigrationServiceImpl extends ServiceImpl<MigrationMapper, Migration>
        implements MigrationService {
    @Override
    public List<String> all() {
        return list().stream().map(Migration::getMigration).toList();
    }

    @Override
    public void store(String name) {
        Migration migration = new Migration();
        migration.setMigration(name);
        save(migration);
    }
}
