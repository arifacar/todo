package com.arifacar.service.config;

import com.arifacar.service.util.RedisUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfiguration {

    private final RedisUtil redisUtil;

    @Autowired
    public CacheConfiguration(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Bean
    LoadingCache<String, Object> userCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(12, TimeUnit.HOURS)
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) {
                        return redisUtil.getFromCache(key);
                    }
                });
    }

    @Bean
    LoadingCache<String, Object> todoCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(12, TimeUnit.HOURS)
                .build(new CacheLoader<String, Object>() {

                    @Override
                    public Object load(String key) {
                        return redisUtil.getFromCache(key);
                    }
                });
    }
}
