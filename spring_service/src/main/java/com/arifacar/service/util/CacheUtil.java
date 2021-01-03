package com.arifacar.service.util;

import com.arifacar.domain.model.constants.Constants;
import com.arifacar.domain.model.constants.CacheConstants;
import com.arifacar.domain.model.todo.TodoItem;
import com.arifacar.domain.model.user.User;
import com.arifacar.domain.util.PropertyUtil;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CacheUtil {

    private final RedisUtil redisUtil;
    private final LoggerUtil logger;
    private final LoadingCache<String, Object> userCache;
    private final LoadingCache<String, Object> todoCache;


    @Autowired
    public CacheUtil(RedisUtil redisUtil, LoggerUtil logger,
                     LoadingCache<String, Object> userCache, LoadingCache<String, Object> todoCache) {
        this.redisUtil = redisUtil;
        this.logger = logger;
        this.userCache = userCache;
        this.todoCache = todoCache;
    }

    public List<User> getFromUserCacheCache(String key) {
        return (List<User>) getFromCache(userCache, key);
    }

    public void setToUserListCache(String key, List<User> value) {
        if (value != null)
            setToCache(userCache, key, value);
    }

    public List<TodoItem> getFromTodoItemCache(String key) {
        return (List<TodoItem>) getFromCache(todoCache, key);
    }

    public void setToTodoItemListCache(String key, List<TodoItem> value) {
        setToCache(todoCache, key, value);
    }

    public void invalidateTodoItemListCache(Long userId) {
        for (int page = 0; page <= 20; page++) { // TODO: Fix here, Todo item list can be bigger than 20 :)
            String key = CacheUtil.createKey(CacheConstants.TODO_ITEM_LIST, String.valueOf(1), String.valueOf(userId));
            redisUtil.clearFromCache(key);
        }
        todoCache.invalidateAll();
    }

    public void invalidateAllCache() {
        redisUtil.flushAll();
        todoCache.invalidateAll();
        userCache.invalidateAll();
    }

    private Object getFromCache(LoadingCache<String, Object> cache, String key) {
        Object value = null;
        try {
            value = cache.get(key);
        } catch (InvalidCacheLoadException e) {
            logger.logTrace(getClass(), Constants.APP_NAME + "getFromCache", "key:" + key + " e:" + e.getMessage());
        } catch (Exception e) {
            logger.logError(getClass(), Constants.APP_NAME + "getFromCache", "key:" + key + " e:" + e.getMessage(), null);
        }
        return value;
    }

    private void setToCache(LoadingCache<String, Object> cache, String key, Object value) {
        setToCache(cache, key, value, CacheConstants.DEFAULT_EXPIRE_TIME, CacheConstants.DEFAULT_EXPIRE_TIME_UNIT);
    }

    private void setToCache(LoadingCache<String, Object> cache, String key, Object value,
                            long expireTime, TimeUnit timeUnit) {
        cache.put(key, value);
        redisUtil.setToCache(key, value, expireTime, timeUnit);
    }


    public static String createKey(String... keyArray) {
        return createKey(false, keyArray);
    }

    public static String createKey(boolean excludeVersion, String... keyArray) {
        if (keyArray == null || keyArray.length == 0)
            return "";

        StringBuilder builder = new StringBuilder();

        if (!excludeVersion) {
            String cacheVersion = PropertyUtil.getApplicationProperty("cache.version");
            builder.append(cacheVersion).append(Constants.KEY_DELIMETER);
        }

        Arrays.stream(keyArray).forEach(key -> builder.append(key).append(Constants.KEY_DELIMETER));

        return builder.substring(0, builder.length() - 1);
    }
}