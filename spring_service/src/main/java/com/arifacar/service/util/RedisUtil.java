package com.arifacar.service.util;

import com.arifacar.domain.model.constants.CacheConstants;
import com.arifacar.domain.model.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUtil {

    private final RedisTemplate<Object, Object> redisTemplate;

    private final LoggerUtil logger;

    @Autowired
    public RedisUtil(RedisTemplate<Object, Object> redisTemplate, LoggerUtil logger) {
        this.redisTemplate = redisTemplate;
        this.logger = logger;
    }

    @PostConstruct
    private void info() {
        if (redisTemplate == null) {
            logger.logWarn(getClass(), "info", "redisTemplate is null.");
        } else {
            RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();

            if (connectionFactory == null) {
                logger.logWarn(getClass(), "info", "connectionFactory is null");
                return;
            }

            String hostname = ((JedisConnectionFactory) connectionFactory).getHostName();
            int port = ((JedisConnectionFactory) redisTemplate.getConnectionFactory()).getPort();
            logger.logDebug(getClass(), "info", "Redis connection is establising to " + hostname + ":" + port);
        }
    }

    public void setToCache(String key, Object value) {
        setToCache(key, value, CacheConstants.DEFAULT_EXPIRE_TIME, CacheConstants.DEFAULT_EXPIRE_TIME_UNIT);
    }

    public void setToCache(String key, Object value, long expireTime, TimeUnit timeunit) {
        try {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, expireTime, timeunit);
        } catch (Exception e) {
            logger.logError(getClass(), Constants.APP_NAME + "setToCache", "Redis connection exception for key:" + key + " e:" + e.getMessage(), null);
        }
    }

    public Object getFromCache(String key) {
        logger.logTrace(getClass(), "getFromCache", "key:" + key);
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.logError(getClass(), Constants.APP_NAME + "getFromCache", "Redis connection exception for key:" + key + " e:" + e.getMessage(), null);
        }
        return null;
    }

    public void clearFromCache(String key) {
        logger.logDebug(getClass(), "clearFromCache", "key:" + key);
        redisTemplate.opsForValue().set(key, null);
    }

    // Servisten key list bos gelirse bu metodla tum key'ler silinecek.
    public void flushAll() {
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();

        if (connectionFactory == null) {
            logger.logWarn(getClass(), "flushAll", "connectionFactory is null");
            return;
        }

        RedisConnection conn = connectionFactory.getConnection();

        if (conn != null)
            conn.flushAll();

        logger.logDebug(getClass(), "flushAll", "flushed all cache");
    }
}