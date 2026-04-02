package xyz.learnhub.course.caches;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.learnhub.common.exception.ServiceException;
import xyz.learnhub.common.util.MemoryCacheUtil;
import xyz.learnhub.common.util.StringUtil;
import xyz.learnhub.course.bus.UserBus;

/**
 *
 * @create 2023/3/20 15:20
 */
@Component
public class UserCanSeeCourseCache {

    @Autowired private UserBus userBus;

    private static final String keyTemplate = "c:%d-u:%d";

    private static final int expire = 3600; // s

    public boolean check(Integer userId, Integer courseId, boolean isThrow)
            throws ServiceException {
        boolean result;
        if (MemoryCacheUtil.exists(key(userId, courseId))) {
            String cacheResult = (String) MemoryCacheUtil.get(key(userId, courseId));
            result = "1".equals(cacheResult);
        } else {
            result = userBus.canSeeCourse(userId, courseId);
            put(userId, courseId, result);
        }
        if (!result && isThrow) {
            throw new ServiceException("无权限观看");
        }
        return result;
    }

    public void put(Integer userId, Integer courseId, boolean result) {
        MemoryCacheUtil.set(key(userId, courseId), result ? "1" : "0", expire);
    }

    public void destroy(List<Integer> userIds, Integer courseId) {
        if (StringUtil.isNotEmpty(userIds)) {
            List<String> keyList = new ArrayList<>();
            for (Integer userId : userIds) {
                keyList.add(key(userId, courseId));
            }
            MemoryCacheUtil.del(keyList.toArray(new String[keyList.size()]));
        }
    }

    private String key(Integer userId, Integer courseId) {
        return String.format(keyTemplate, courseId, userId);
    }
}
