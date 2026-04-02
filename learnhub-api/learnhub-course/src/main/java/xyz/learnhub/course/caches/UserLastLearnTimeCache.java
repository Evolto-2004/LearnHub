package xyz.learnhub.course.caches;

import org.springframework.stereotype.Component;
import xyz.learnhub.common.util.MemoryCacheUtil;

/**
 *
 * @create 2023/3/22 13:57
 */
@Component
public class UserLastLearnTimeCache {

    private static final String groupName = "user-learn-last-timestamp";

    private static final int expire = 9500; // 9.5s

    public Long get(Integer userId) {
        return (Long) MemoryCacheUtil.hGet(groupName, userId + "");
    }

    public void put(Integer userId, Long timestamp) {
        MemoryCacheUtil.hSet(groupName, userId + "", timestamp);
    }
}
